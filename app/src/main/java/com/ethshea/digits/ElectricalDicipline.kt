import com.ethshea.digits.AtomicHumanUnit
import com.ethshea.digits.HumanUnit
import com.ethshea.digits.UnitSystem
import com.ethshea.digits.evaluator.Quantity


val prefixMagStart = -15
val prefixes = listOf("f", "p", "n", "μ", "m", "", "k", "M", "G", "T")
val preferredUnits = listOf(
        UnitSystem.byAbbreviation("Ω")!!,
        UnitSystem.byAbbreviation("A")!!,
        UnitSystem.byAbbreviation("V")!!
)

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanUnit {
    val expandedQuantity = quantity.value * quantity.unit.factor
    val prefixIndex = (Math.log10(expandedQuantity.abs().toDouble()).toInt() - prefixMagStart) / 3 // Double is not super accurate, but should be good enough
    val prefixMagnitude = (prefixIndex * 3) + prefixMagStart

    val possibleUnits = UnitSystem.prefixUnits + preferredUnits + UnitSystem.units
    var reducedUnit = quantity.unit
    val composedUnits = mutableMapOf<AtomicHumanUnit, Int>()

    trials@
    while (!reducedUnit.representationEqual(UnitSystem.void)) {
        for (unit in possibleUnits){
            if (unit.fitsWithin(reducedUnit)) {
                composedUnits[unit] = composedUnits.getOrDefault(unit, 0) + 1
                reducedUnit -= unit
                continue@trials
            }
        }

        // This code should never really execute because there should be a base unit for each dimension (like meters or seconds)
        // Including this code guarantees that this function terminates
        val extraUnit = AtomicHumanUnit("Unk", "+extra", reducedUnit.dimensions, reducedUnit.factor)
        composedUnits[extraUnit] = 1
        break
    }

    return HumanUnit(composedUnits)
}