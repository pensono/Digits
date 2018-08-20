import com.ethshea.digits.HumanUnit
import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import com.ethshea.digits.evaluator.Quantity
import java.math.BigDecimal


val prefixMagStart = -15
val prefixes = listOf("f", "p", "n", "μ", "m", "", "k", "M", "G", "T")
val preferredUnits = listOf(
        UnitSystem.byAbbreviation("A")!!,
        UnitSystem.byAbbreviation("V")!!,
        UnitSystem.byAbbreviation("Ω")!!
)

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanUnit {
    val expandedQuantity = quantity.value * quantity.unit.factor
    val prefixIndex = (Math.log10(expandedQuantity.abs().toDouble()).toInt() - prefixMagStart) / 3 // Double is not super accurate, but should be good enough
    val prefixMagnitude = (prefixIndex * 3) + prefixMagStart

    val possibleUnits = preferredUnits + UnitSystem.units
    var reducedUnit = quantity.unit
    val composedUnits = mutableListOf<HumanUnit>()
    while (reducedUnit != UnitSystem.void) {
        for (unit in possibleUnits){
            if (reducedUnit.fits(unit)) {
                composedUnits += unit
                reducedUnit -= unit
            }
        }
    }

    val abbreviation = composedUnits.joinToString("") {u -> u.abbreviation}
    val name = composedUnits.joinToString(" ") {u -> u.name}
    return HumanUnit(abbreviation, name, (-quantity.unit).dimensions, BigDecimal.ONE.movePointLeft(prefixMagnitude))
}