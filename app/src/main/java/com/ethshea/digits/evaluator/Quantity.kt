package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import java.math.BigDecimal
import java.math.BigInteger

/**
 * @author Ethan
 */

data class Quantity(val value:BigDecimal, val unit: NaturalUnit? = null)