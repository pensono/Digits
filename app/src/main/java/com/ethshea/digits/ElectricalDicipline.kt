import com.ethshea.digits.AtomicHumanUnit
import com.ethshea.digits.HumanUnit
import com.ethshea.digits.UnitSystem
import com.ethshea.digits.evaluator.Quantity
import java.util.*


val prefixMagStart = -15
val prefixes = listOf("f", "p", "n", "μ", "m", "", "k", "M", "G", "T")
val preferredUnits = listOf(
        UnitSystem.unitByAbbreviation("Ω")!!,
        UnitSystem.unitByAbbreviation("V")!!,
        UnitSystem.unitByAbbreviation("A")!!
)

/**
 * Returns an inverse unit that can be used to normalize the result
 */
fun humanize(quantity: Quantity) : HumanUnit {
    val visitedUnits = mutableSetOf<HumanUnit>()
    val visitQueue = PriorityQueue<HumanUnit>(compareBy(HumanUnit::exponentMagnitude))

    // This is not a very good way of dealing with prefixes
    visitQueue.addAll(UnitSystem.prefixes.map{ HumanUnit(mapOf(it to 1)) })
    visitQueue.add(HumanUnit(mapOf())) // No prefix

    while (visitQueue.isNotEmpty()) {
        val currentUnit = visitQueue.poll()
        visitedUnits.add(currentUnit)

        if (currentUnit.representationEqual(quantity.unit)) {
            return currentUnit
        }

        // I'm not 100% on the fact that this temporarily creates a human unit that has atomic units with degree 0
        visitQueue.addAll(UnitSystem.units.map(currentUnit::plus).filter{ !visitedUnits.contains(it) && !it.components.values.contains(0) })
        visitQueue.addAll(UnitSystem.units.map(currentUnit::minus).filter { !visitedUnits.contains(it) && !it.components.values.contains(0) })
    }

    // This code should never really execute because there should be a base unit for each dimension (like meters or seconds)
    val extraUnit = AtomicHumanUnit("Unk", "Unknown", quantity.unit.dimensions, quantity.unit.factor)
    return HumanUnit(mapOf(extraUnit to 1))
}