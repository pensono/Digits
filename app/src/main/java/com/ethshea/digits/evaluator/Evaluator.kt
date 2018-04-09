package com.ethshea.digits.evaluator

import com.ethshea.digits.NaturalUnit
import com.ethshea.digits.UnitSystem
import java.math.BigDecimal
import java.util.*

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val position: Int = 0)

data class ParseResult<T>(val value: T, val errors: Collection<ErrorMessage>)

fun <T> failure(value: T, message: String) = ParseResult(value, listOf(ErrorMessage(message)))
fun <O, N> failure(prev: ParseResult<O>, message: String, operation: (O) -> N) = ParseResult(operation.invoke(prev.value), prev.errors.plusElement(ErrorMessage(message)))
fun <O1, O2, N> failure(first: ParseResult<O1>, second: ParseResult<O2>, message: String, operation: (O1, O2) -> N) =
        ParseResult(operation.invoke(first.value, second.value), (first.errors + second.errors).plusElement(ErrorMessage(message)))
fun <T> success(value: T) = ParseResult(value, listOf())
fun <O, N> success(prev: ParseResult<O>, operation: (O) -> N) = ParseResult(operation.invoke(prev.value), prev.errors)
fun <O1, O2, N> success(first: ParseResult<O1>, second: ParseResult<O2>, operation: (O1, O2) -> N) = ParseResult(operation.invoke(first.value, second.value), first.errors + second.errors)
fun <O1, O2, N> bind(first: ParseResult<O1>, second: ParseResult<O2>, operation: (O1, O2) -> ParseResult<N>) : ParseResult<N> {
    val result = operation.invoke(first.value, second.value)
    return ParseResult(result.value, result.errors + first.errors + second.errors)
}

fun evaluateExpression(input: String) : ParseResult<Quantity> {
    return evaluateExpression(TokenIterator(input))
}

private data class OperatorSpec(val arity: Int, val prescience: Int, val operation: (List<ParseResult<Quantity>>) -> ParseResult<Quantity>)

private val operators = mapOf(
        "+" to OperatorSpec(2, 1, unitInference(Quantity::plus)),
        "-" to OperatorSpec(2, 1, unitInference(Quantity::minus)),
        "×" to OperatorSpec(2, 3, binary(Quantity::times)),
        "/" to OperatorSpec(2,3, binary(Quantity::div))
//        "sin" to OperatorSpec(1, 5, unary({q -> Quantity(q.value) }))
)

// I don't like that this takes a list. Is there some way it can be combined with binary()?
fun unitInference(operator: (Quantity, Quantity) -> Quantity) : (List<ParseResult<Quantity>>) -> ParseResult<Quantity> =
     {list ->
         if (list[0].value.unit.dimensionallyEqual(list[1].value.unit)) {
             success(list[0], list[1], operator)
         } else {
             failure(list[0], list[1], "Incompatible units", {a, b ->
                 operator.invoke(a, Quantity(b.value, a.unit))
             })
         }
     }


fun binary(operator: (Quantity, Quantity) -> Quantity): (List<ParseResult<Quantity>>) -> ParseResult<Quantity> =
    {list -> success(list[0], list[1], {a, b -> operator.invoke(a, b)})}


fun unary(operator: (Quantity) -> Quantity): (List<Quantity>) -> Quantity =
    {list -> operator.invoke(list[0])}


fun <T> Stack<T>.pop(amount: Int) : List<T> {
    var result = ArrayList<T>()
    for (i in 0 until amount) {
        result.add(0,this.pop())
    }
    return result
}

fun evaluateExpression(tokens: TokenIterator) : ParseResult<Quantity> {
    // Shunting-yard
    val operatorStack = Stack<OperatorSpec>()
    val quantityStack = Stack<ParseResult<Quantity>>()

    var minusIsUnary = true // True at start and after operators

    while (tokens.hasNext() && !tokens.isNext(')')) {
        if (tokens.isNext('(')) {
            tokens.consume('(')
            quantityStack.push(evaluateExpression(tokens))
            tokens.consume(')')
        } else if (isNextNumeric(tokens) || (tokens.isNext('-') && minusIsUnary)) {
            quantityStack.push(parseNumeric(tokens))
            minusIsUnary = false
        } else {
            val operator = tokens.takeNextLargest(operators)
            if (operator != null) {
                while (!operatorStack.empty() && operatorStack.peek().prescience >= operator.prescience) {
                    val toApply = operatorStack.pop()
                    val arguments = quantityStack.pop(toApply.arity)
                    quantityStack.push(toApply.operation.invoke(arguments))
                }
                operatorStack.push(operator)
                minusIsUnary = true
            } else {
                break // TODO handle fail case better
            }
        }
    }

    while (operatorStack.isNotEmpty()) {
        val toApply = operatorStack.pop()
        if (quantityStack.size < toApply.arity) return quantityStack.pop() // Lame. We parsed most of the expression. Should return an error
        val arguments = quantityStack.pop(toApply.arity)
        quantityStack.push(toApply.operation.invoke(arguments))
    }

    // Quantity stack must have 1 thing left

    return quantityStack.pop()
}

fun parseNumeric(tokens: TokenIterator) : ParseResult<Quantity> {
    var value = ""
    while (tokens.hasNext() && (isNextNumeric(tokens) || (value.isEmpty() && tokens.isNext('-')))) {
        value += tokens.next()
    }

    val unit = parseUnit(tokens)

    return try {
        success(unit, {u -> Quantity(BigDecimal(value), u)})
    } catch (e: NumberFormatException) {
        failure(unit, value + " is not a valid number", {u -> Quantity(BigDecimal(1), u)})
    }
}

private fun isNextNumeric(tokens: TokenIterator) =
        (tokens.peek().isDigit() || tokens.isNext('.'))

fun parseUnit(tokens: TokenIterator) : ParseResult<NaturalUnit> {
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

    return if (foundUnit) success(unit) else failure(unit, "Unit failure")
}