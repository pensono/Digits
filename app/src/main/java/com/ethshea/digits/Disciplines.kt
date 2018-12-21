package com.ethshea.digits

import com.ethshea.digits.units.AtomicHumanUnit
import com.ethshea.digits.units.UnitSystem

/**
 * @author Ethan
 */

fun u(abbr: String) = UnitSystem.unitAbbreviations[abbr]!!

data class Discipline(val nameResource: Int, val iconResource: Int, val units: List<AtomicHumanUnit>)

val disciplines = listOf(
        Discipline(R.string.discipline_astronomical, R.drawable.ic_telescope_black_24dp, listOf()),
        Discipline(R.string.discipline_carpentry, R.drawable.ic_hand_saw, listOf()),
        Discipline(R.string.discipline_chemistry, R.drawable.ic_flask_black_24dp, listOf()),
        Discipline(R.string.discipline_data, R.drawable.ic_bits, listOf()), // 1001 design
        Discipline(R.string.discipline_electrical, R.drawable.ic_flash_on_black_24dp, listOf(u("V"), u("A"), u("Ω"))),
        Discipline(R.string.discipline_mechanical, R.drawable.ic_two_gears, listOf()),
        Discipline(R.string.discipline_physics, R.drawable.ic_atom_black_24dp, listOf()),
        Discipline(R.string.discipline_signals, R.drawable.ic_current_ac_black_24dp, listOf(u("Hz"))), // Sine wave
        Discipline(R.string.discipline_surveying, R.drawable.ic_pine_tree_black_24dp, listOf())
)
