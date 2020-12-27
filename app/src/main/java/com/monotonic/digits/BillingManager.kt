package com.monotonic.digits

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.android.billingclient.api.*

class BillingManager(val context: Context) : PurchasesUpdatedListener, AcknowledgePurchaseResponseListener {
    companion object {
        const val PRO_SKU = "com.monotonic.digits.pro"
    }

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases() // GP requires this be called
            .build()

    // SKU to its details
    private var skuDetails: Map<String, SkuDetails> = mapOf()

    var hasPro: Boolean = false
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
        val purchasesResult = billingClient.queryPurchases(BillingClient.SkuType.INAPP)
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
            Log.w(MainActivity.TAG, "Purchase state is pending.")
        }
    }

    fun refreshSkuDetails() {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(listOf(PRO_SKU)).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                skuDetails = skuDetailsList.associateBy { it.sku }
            } else {
                logError(billingResult)
            }
        }

    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: List<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> purchases?.onEach { handlePurchase(it) }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                // Handle an error caused by a user cancelling the purchase flow.
            }
            else -> logError(billingResult)
        }
    }

    override fun onAcknowledgePurchaseResponse(billingResult: BillingResult) {
        // Not sure what to do here
        Log.i(MainActivity.TAG, "Purchase acknowledged")
    }

    fun doProPurchase(activity: Activity) {
        val proSku = skuDetails[PRO_SKU]

        if (proSku == null) {
            refreshSkuDetails()

            AlertDialog.Builder(activity)
                    .setMessage("The purchase cannot be started at this time.")
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            return
        }

        val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(proSku)
                .build()
        val responseCode = billingClient.launchBillingFlow(activity, flowParams)

        Log.i(MainActivity.TAG, "Billing complete. Response code: $responseCode")
    }
}

private fun logError(billingResult: BillingResult?) {
    Log.e(MainActivity.TAG, "Billing error: ${billingResult?.debugMessage} Error Code: ${billingResult?.responseCode}")
}
