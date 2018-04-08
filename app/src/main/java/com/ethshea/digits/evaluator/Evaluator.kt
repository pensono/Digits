package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal
import java.util.*

/**
 * @author Ethan
 */

sealed class ParseResult {
    class Success(var root: Any) : ParseResult()
    class Failure(var message: String, val location: Int)
}

fun evaluateExpression(input: String) : Quantity? {
    return evaluateExpression(TokenIterator(input))
}

private data class OperatorSpec(val arity: Int, val prescidence: Int, val operation: (List<Quantity>) -> Quantity)

private val operators = mapOf(
        "+" to OperatorSpec(2, 1, binary(Quantity::plus)),
        "-" to OperatorSpec(2, 1, binary(Quantity::minus)),
        "*" to OperatorSpec(2, 3, binary(Quantity::times)),
        "/" to OperatorSpec(2,3, binary(Quantity::div))
//        "sin" to OperatorSpec(1, 5, unary({q -> Quantity(q.value) }))
)

fun binary(operator: (Quantity, other: Quantity) -> Quantity): (List<Quantity>) -> Quantity {
    return {list -> operator.invoke(list[0], list[1])}
}

fun unary(operator: (Quantity) -> Quantity): (List<Quantity>) -> Quantity {
    return {list -> operator.invoke(list[0])}
}

fun <T> Stack<T>.pop(amount: Int) : List<T> {
    var result = ArrayList<T>()
    for (i in 0 until amount) {
        result.add(0,this.pop())
    }
    return result
}

fun evaluateExpression(tokens: TokenIterator) : Quantity? {
    // Shunting-yard
    val operatorStack = Stack<OperatorSpec>()
    val quantityStack = Stack<Quantity>()

    var minusIsUnary = true // True at start and after operators

    while (tokens.hasNext() && !tokens.isNext(')')) {
        if (isNextNumeric(tokens) || (tokens.isNext('-') && minusIsUnary)) {
            quantityStack.push(parseNumeric(tokens))
            minusIsUnary = false
        } else {
            val operator = tokens.takeNextLargest(operators)
            if (operator != null) {
                while (!operatorStack.empty() && operatorStack.peek().prescidence >= operator.prescidence) {
                    val toApply = operatorStack.pop()
                    val arguments = quantityStack.pop(toApply.arity)
                    quantityStack.push(toApply.operation.invoke(arguments))
                }
                operatorStack.push(operator)
                minusIsUnary = true
            } else {
                return null
            }
        }
    }

    while (operatorStack.isNotEmpty()) {
        val toApply = operatorStack.pop()
        val arguments = quantityStack.pop(toApply.arity)
        quantityStack.push(toApply.operation.invoke(arguments))
    }

    // Quantity stack must have 1 thing left

    return quantityStack.pop()
}

fun parseNumeric(tokens: TokenIterator) : Quantity? {
    var value = ""
    while (tokens.hasNext() && (isNextNumeric(tokens) || (value.isEmpty() && tokens.isNext('-')))) {
        value += tokens.next()
    }

    val unit = parseUnit(tokens)

    return try {
        Quantity(BigDecimal(value), unit)
    } catch (e: NumberFormatException) {
        null
    }
}

private fun isNextNumeric(tokens: TokenIterator) =
        (tokens.peek().isDigit() || tokens.isNext('.'))

fun parseUnit(tokens: TokenIterator) : NaturalUnit? {
    var invert = false
    var unit = UnitSystem.void
    var foundUnit = false

    while (tokens.hasNext()) {
        val abbreviation = tokens.nextLargest(UnitSystem.abbreviations)
        if (abbreviation != null) {
            val newUnit = UnitSystem.byAbbreviation(abbreviation)!!
            unit += if (invert) -newUnit else newUnit
            tokens.consume(abbreviation)
            foundUnit = true
        } else if (tokens.isNext("/") && tokens.nextLargest(UnitSystem.abbreviations.map { a -> "/" + a }) != null) { // Lame fix for division at the end
            invert = true
            tokens.consume("/")
        } else {
            break
        }
    }

    return if (foundUnit) unit else null
}