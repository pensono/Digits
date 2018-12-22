package com.ethshea.digits

import com.ethshea.digits.units.AtomicHumanUnit
import com.ethshea.digits.units.UnitSystem

/**
 * @author Ethan
 */

fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!

data class Discipline(val nameResource: Int, val iconResource: Int, val units: List<AtomicHumanUnit>)

val disciplines = listOf(
        Discipline(R.string.discipline_astronomical, R.drawable.ic_telescope_black_24dp, listOf(u("m"), u("°"), u("sr"))),
        Discipline(R.string.discipline_carpentry, R.drawable.ic_hand_saw, listOf(u("m"))),
        Discipline(R.string.discipline_chemistry, R.drawable.ic_flask_black_24dp, listOf(u("mol"))),
        Discipline(R.string.discipline_data, R.drawable.ic_bits, listOf(u("b"))),
        Discipline(R.string.discipline_electrical, R.drawable.ic_flash_on_black_24dp, listOf(u("V"), u("A"), u("Ω"), u("W"), u("F"), u("T"), u("H"), u("C"), u("°"), u("sr"), u("S"), u("s"))),
        Discipline(R.string.discipline_fluids, R.drawable.ic_waves_black_24dp, listOf(u("V"), u("A"), u("Ω"))),
        Discipline(R.string.discipline_mechanical, R.drawable.ic_two_gears, listOf(u("m"), u("Pa"), u("Hz"), u("N"), u("g"))),
        Discipline(R.string.discipline_physics, R.drawable.ic_atom_black_24dp, listOf(u("Wb"), u("C"), u("Hz"), u("g"), u("Pa"))),
        Discipline(R.string.discipline_signals, R.drawable.ic_current_ac_black_24dp, listOf(u("Hz"))),
        Discipline(R.string.discipline_surveying, R.drawable.ic_pine_tree_black_24dp, listOf(u("m")))
)
