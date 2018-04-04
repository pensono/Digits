package com.ethshea.unitcalculator.evaluator

import com.ethshea.unitcalculator.dimensional.SIUnit
import java.math.BigInteger

/**
 * @author Ethan
 */
data class NumericNode(val value:BigInteger, val unit: SIUnit)