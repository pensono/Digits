package com.monotonic.digits.units

import android.provider.Settings.Global.getString
import com.monotonic.digits.R
import com.monotonic.digits.evaluator.Precision
import com.monotonic.digits.evaluator.SciNumber
import com.monotonic.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */
class PrefixUnit(abbreviation: String, nameResourceId: Int, val exponent: Int, val descriptionResourceId: Int) :
        AtomicHumanUnit(abbreviation, nameResourceId, "1e$exponent", DimensionBag(), SciNumber.Real("1e$exponent", Precision.Infinite)) {
    companion object {
        val One = PrefixUnit("", R.string.prefix_name_one, 0, R.string.prefix_description_one)
    }

    override operator fun times(n: Int): PrefixUnit =
            PrefixUnit(abbreviation, nameResourceId, exponent * n, R.string.prefix_name_unknown) // Strings here may not make sense. It shouldn't show up in the UI so use unknown for now.

    override fun equals(other: Any?): Boolean = other is PrefixUnit && other.abbreviation == abbreviation && other.nameResourceId == nameResourceId && factor == other.factor
}
