package com.monotonic.digits

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingManager(val context: Context) : PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {
    companion object {
        const val PRO_SKU = "com.monotonic.digits.pro"
    }

    private val billingClient : BillingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases() // GP requires this be called
            .build()

    // SKU to its details
    private var skuDetails: Map<String, SkuDetails> = mapOf()

    public var hasPro : Boolean = false
        private set

    init {
        connectToBillingService()
    }

    fun connectToBillingService() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    refreshPurchases()
                    refreshSkuDetails()
                    Log.i(MainActivity.TAG, "Connected to billing service")
                }
            }
            override fun onBillingServiceDisconnected() {
                connectToBillingService()
            }
        })
    }

    fun refreshPurchases() {
        val purchasesResult: Purchase.PurchasesResult =
                billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        purchasesResult.purchasesList?.onEach { handlePurchase(it) }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                billingClient.acknowledgePurchase(acknowledgePurchaseParams, this)
            }

            if (purchase.sku == PRO_SKU) {
                hasPro = true
            }
        } else if (purchase.purchaseState == Purchase.PurchaseState.PENDING) {
            // Not quite purchased yet. We can't grant anything
            // We're supposed to inform the user that this has occurred but that's not
            // necessary since there's no situation when this would occur
        }
    }

    fun refreshSkuDetails() {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(listOf(PRO_SKU)).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                skuDetails = skuDetailsList.associateBy { it.sku }
            } else {
                logError(billingResult)
            }
        }

    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when {
            billingResult.responseCode == BillingClient.BillingResponseCode.OK -> purchases?.onEach { handlePurchase(it) }
            billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED -> {
                // Handle an error caused by a user cancelling the purchase flow.
            }
            else -> // Handle any other error codes.
                logError(billingResult)
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult?) {
        // Not sure what to do here
        Log.i(MainActivity.TAG, "Purchase acknowledged")
    }

    fun doProPurchase(activity: Activity) {
        // What if skuDetails does not contain an entry for the PRO SKU?
        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetails[PRO_SKU])
                .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams)
    }

}

private fun logError(billingResult: BillingResult?) {
    Log.e(MainActivity.TAG, "Billing error: ${billingResult?.debugMessage} Error Code: ${billingResult?.responseCode}")
}
