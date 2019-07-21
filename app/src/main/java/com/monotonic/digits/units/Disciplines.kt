package com.monotonic.digits.units

import com.monotonic.digits.R
import com.monotonic.digits.human.AtomicHumanUnit

/**
 * @author Ethan
 */

fun u(abbr: String) = UnitSystem.unitAbbreviations.getValue(abbr)

data class Discipline(val nameResource: Int, val iconResource: Int, val units: List<AtomicHumanUnit>)

val disciplines = listOf(
        Discipline(R.string.discipline_basic, 0, listOf(u("m"), u("g"), u("L"), u("s"), u("hr"), u("min"), u("day"), u("yr"))),
        Discipline(R.string.discipline_basic_us, 0, listOf(u("in"), u("ft"), u("mi"), u("floz"), u("gal"), u("lb"), u("s"), u("hr"), u("min"), u("day"), u("yr"))),
        Discipline(R.string.discipline_astronomical, R.drawable.ic_telescope_black_24dp, listOf(u("m"), u("°"), u("sr"), u("au"), u("pc"), u("ly"))),
        Discipline(R.string.discipline_carpentry, R.drawable.ic_hand_saw, listOf(u("m"), u("in"), u("ft"))),
        Discipline(R.string.discipline_cooking, R.drawable.ic_telescope_black_24dp, listOf(u("g"), u("L"), u("tsp"), u("tbsp"), u("pt"), u("qt"), u("gal"), u("floz"))), // Fahrenheit, celsius
        Discipline(R.string.discipline_chemistry, R.drawable.ic_flask_black_24dp, listOf(u("mol"), u("m"), u("L"), u("Pa"), u("atm"), u("Torr"))), // kelvin, celsius
//        Discipline(R.string.discipline_data, R.drawable.ic_bits, listOf(u("b"))),
        Discipline(R.string.discipline_electrical, R.drawable.ic_flash_on_black_24dp, listOf(u("V"), u("A"), u("Ω"), u("s"), u("F"), u("H"), u("T"), u("W"), u("C"), u("S"))),
//        Discipline(R.string.discipline_fluids, R.drawable.ic_waves_black_24dp, listOf(u("Pa"))),
        Discipline(R.string.discipline_mechanical, R.drawable.ic_two_gears, listOf(u("m"), u("Pa"), u("Hz"), u("N"), u("g"))),
        Discipline(R.string.discipline_physics, R.drawable.ic_atom_black_24dp, listOf(u("g"), u("Pa"), u("u"), u("N"), u("Hz"), u("Wb"), u("C"))),
//        Discipline(R.string.discipline_signals, R.drawable.ic_current_ac_black_24dp, listOf(u("Hz"))),
        Discipline(R.string.discipline_surveying, R.drawable.ic_pine_tree_black_24dp, listOf(u("m"), u("a"), u("ha"), u("in"), u("ft"), u("mi"), u("ac")))
)
