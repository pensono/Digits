package com.ethshea.unitcalculator.evaluator

/**
 * @author Ethan
 */

//enum class Prefix(symbol: String) {
//    FEMTO("f"), PICO("p"), NANO("n"), MICRO("μ"), CENTI("c"), ONE(""), KILO("k"), MEGA("M"), GIGA("G"), TERA("T"), PETA("P")
//}

class SIUnit(val meter: Int, val gram: Int, val second: Int, val coulomb: Int, val exponent: Int) {
    constructor(meter: Int, gram: Int, second: Int, coulomb: Int) : this(meter, gram, second, coulomb, 0)
    constructor() : this(0, 0, 0, 0, 0)

    operator fun plus(other: SIUnit) : SIUnit {
        return SIUnit(meter + other.meter, gram + other.gram, second + other.second, coulomb + other.coulomb, exponent + other.exponent)
    }

    operator fun minus(other: SIUnit) : SIUnit {
        return SIUnit(meter - other.meter, gram - other.gram, second - other.second, coulomb - other.coulomb, exponent - other.exponent)
    }

    operator fun unaryMinus(): SIUnit {
        return SIUnit(-meter, -gram, -second, -coulomb, -exponent)
    }

    companion object {
        val unitMap = hashMapOf(
            "m" to SIUnit(1, 0, 0, 0),
                "g" to SIUnit(0, 1, 0, 0, 0),
                "s" to SIUnit(0, 0, 1, 0),
                "Hz" to SIUnit(0, 0, -1, 0),
                "k" to SIUnit(0, 0, 0, 0, 3)
        )

        fun fromString(input: String) : SIUnit? {
            var unit = SIUnit()
            var remaining = input
            var invert = false

            outer@ while (remaining.isNotEmpty()) {
                for (entry in unitMap.entries.sortedBy { -it.key.length }) {
                    if (remaining.startsWith(entry.key)) {
                        unit += if (invert) -entry.value else entry.value
                        remaining = remaining.substring(entry.key.length)
                        continue@outer
                    }
                }

                if (remaining.startsWith("/")) {
                    invert = true
                    remaining = remaining.substring(1)
                } else {
                    return null
                }
            }

            return unit
        }
    }

    override fun equals(other: Any?): Boolean {
        return (other is SIUnit) &&
            meter == other.meter && gram == other.gram && second == other.second && coulomb == other.coulomb && exponent == other.exponent
    }

    override fun toString(): String {
        return "SIUnit(m: $meter, g: $gram, s: $second, c: $coulomb, e: $exponent)"
    }
}