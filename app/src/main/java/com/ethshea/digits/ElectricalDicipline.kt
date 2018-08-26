import com.ethshea.digits.units.AtomicHumanUnit
import com.ethshea.digits.units.HumanUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.evaluator.Quantity
import com.ethshea.digits.units.PrefixUnit
import java.util.*


val prefixMagStart = -15
val prefixes = listOf("f", "p", "n", "μ", "m", "", "k", "M", "G", "T")

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanUnit {
    val expandedQuantity = quantity.normalizedValue
    val prefixIndex = (Math.log10(expandedQuantity.abs().toDouble()).toInt() - prefixMagStart) / 3 // Double is not super accurate, but should be good enough
    val prefixExponent = (prefixIndex * 3) + prefixMagStart
    val prefixUnit = PrefixUnit(prefixes[prefixIndex], "prefix", factor = SciNumber(1).pow(prefixExponent))

    val visitedUnits = mutableSetOf<HumanUnit>()
    val visitQueue = PriorityQueue<HumanUnit>(compareBy(HumanUnit::exponentMagnitude))

    visitQueue.add(HumanUnit(mapOf())) // No prefix

    while (visitQueue.isNotEmpty()) {
        val currentUnit = visitQueue.poll()
        visitedUnits.add(currentUnit)

        if (currentUnit.dimensionallyEqual(quantity.unit)) {
            return currentUnit.withPrefix(prefixUnit)
        }

        // I'm not 100% on the fact that this temporarily creates a human unit that has atomic units with degree 0
        visitQueue.addAll(UnitSystem.units.map(currentUnit::plus).filter{ !visitedUnits.contains(it) && !it.components.values.contains(0) })
        visitQueue.addAll(UnitSystem.units.map(currentUnit::minus).filter { !visitedUnits.contains(it) && !it.components.values.contains(0) })
    }

    // This code should never really execute because there should be a base unit for each dimension (like meters or seconds)
    val extraUnit = AtomicHumanUnit("Unk", "Unknown", quantity.unit.dimensions, quantity.unit.factor)
    return HumanUnit(mapOf(extraUnit to 1))
}