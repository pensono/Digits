package com.ethshea.digits.evaluator

import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.parser.DigitsLexer
import com.ethshea.digits.parser.DigitsParser
import com.ethshea.digits.parser.DigitsParserBaseVisitor
import com.ethshea.digits.isNumber
import com.ethshea.digits.parseNumber
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.Interval

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val location: Interval)

// TODO Why does this have to be generic?
data class ParseResult<T>(val value: T, val location: Interval, val errors: Collection<ErrorMessage> = listOf()) {
    constructor(value: T, location: Interval, error: ErrorMessage) : this(value, location, listOf(error))
    constructor(value: T, location: Interval, errorMessage: String) : this(value, location, ErrorMessage(errorMessage, location))

    fun <R, A> invoke(argument: A, operation: (T, A) -> R) = ParseResult(operation(value, argument), location, errors)
    fun <R> invoke(operation: (T) -> R) = ParseResult(operation(value), location, errors)

    fun <R, A> combine(argument: ParseResult<A>, newLocation: Interval, operation: (T, A) -> R) = ParseResult(operation(value, argument.value), newLocation, errors + argument.errors)

    fun error(message: ErrorMessage) = ParseResult(value, location, errors + listOf(message))
    fun error(newErrors: Collection<ErrorMessage>) = ParseResult(value, location, errors + newErrors)
}

fun evaluateExpression(input: String) : ParseResult<Quantity> {
    val lexer = DigitsLexer(CharStreams.fromString(input))
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE) // Error messages enabled by default -_-
    val tokens = CommonTokenStream(lexer)
    val parser = DigitsParser(tokens)
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE)

    val syntaxErrors = mutableListOf<ErrorMessage>()
    parser.addErrorListener(object : BaseErrorListener() {
        override fun syntaxError(recognizer: Recognizer<*, *>, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException?) {
            syntaxErrors += ErrorMessage(msg, intervalOf(offendingSymbol as Token) )
        }
    })

    val expression = parser.expression()
    val result = expression?.accept(Evaluator) ?: ParseResult(Quantity.Zero, Interval(0, 0))

    if (expression.sourceInterval != Interval(0, input.length - 1)) {
        syntaxErrors += ErrorMessage("Incomplete parse", Interval(expression.sourceInterval.b + 1, input.length - 1))
    }

    return result.error(syntaxErrors)
}

private fun intervalOf(operation: Token): Interval = Interval.of(operation.startIndex, operation.stopIndex)

// A null ParseResult means use whatever the identity element for the operation is
object Evaluator : DigitsParserBaseVisitor<ParseResult<Quantity>?>() {
    private val constants = mapOf(
            "π" to Math.PI,
            "pi" to Math.PI,
            "e" to Math.E,
            "τ" to Math.PI / 2,
            "φ" to 1.618033988749894848204586834
    )

    private val functions = mapOf(
            "sin" to Quantity::sin,
            "cos" to Quantity::cos,
            "tan" to Quantity::tan,

            "sinh" to Quantity::sinh,
            "cosh" to Quantity::cosh,
            "tanh" to Quantity::tanh,

            "asin" to Quantity::asin,
            "acos" to Quantity::acos,
            "atan" to Quantity::atan
    )

    override fun visitLiteral(ctx: DigitsParser.LiteralContext): ParseResult<Quantity> {
        val isNumeric = { it : Char -> it.isDigit() || it == '.' }
        val text = ctx.value().text

        return if (text.isEmpty())
            ParseResult(Quantity(SciNumber.Zero), ctx.sourceInterval, "Incomplete literal")
        else if (!text.all(isNumeric))
            ParseResult(Quantity(SciNumber(text.filter(isNumeric))), ctx.sourceInterval, "Non-numeric literal")
        else
            ParseResult(Quantity(SciNumber(text)), ctx.sourceInterval)
    }

    override fun visitConstant(ctx: DigitsParser.ConstantContext): ParseResult<Quantity>? {
        val constantName = ctx.text.toLowerCase()

        val constant = constants[constantName]
        return if (constant == null) {
            // Not sure what value to use here
            ParseResult(Quantity.Zero, ctx.sourceInterval, ErrorMessage(ctx.text + " is not a known constant", ctx.sourceInterval))
        } else {
            ParseResult(Quantity(SciNumber(constant)), ctx.sourceInterval)
        }
    }

    override fun visitUnaryMinus(ctx: DigitsParser.UnaryMinusContext): ParseResult<Quantity>? {
        val argument = ctx.expression().accept(this)

        return argument?.invoke(Quantity::unaryMinus)
    }

    override fun visitSumExpression(ctx: DigitsParser.SumExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs.accept(this) ?: ParseResult(Quantity.Zero, ctx.lhs.sourceInterval, ErrorMessage("Inferred lhs in +", intervalOf(ctx.operation)))
        val rhsResult = ctx.rhs.accept(this) ?: ParseResult(Quantity.Zero, ctx.rhs.sourceInterval, ErrorMessage("Inferred rhs in +", intervalOf(ctx.operation)))
        val operation = if (ctx.operation.type == DigitsLexer.PLUS) Quantity::plus else Quantity::minus

        if (lhsResult.value.unit.dimensionallyEqual(rhsResult.value.unit)) {
            return lhsResult.combine(rhsResult, ctx.sourceInterval, operation)
        } else {
            val fixedRhs = rhsResult.invoke { rhs -> Quantity(rhs.value, lhsResult.value.unit) }
            return lhsResult.combine(fixedRhs, ctx.sourceInterval, operation).error(ErrorMessage("Incompatable units", rhsResult.location))
        }
    }

    override fun visitProductExpression(ctx: DigitsParser.ProductExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs.accept(this) ?: ParseResult(Quantity.One, ctx.lhs.sourceInterval, ErrorMessage("Inferred lhs in *", intervalOf(ctx.operation)))
        val rhsResult = ctx.rhs.accept(this) ?: ParseResult(Quantity.One, ctx.rhs.sourceInterval, ErrorMessage("Inferred rhs in *", intervalOf(ctx.operation)))
        val operation = if (ctx.operation.type == DigitsLexer.TIMES) Quantity::times else Quantity::div

        return lhsResult.combine(rhsResult, ctx.sourceInterval, operation)
    }

    override fun visitExponent(ctx: DigitsParser.ExponentContext): ParseResult<Quantity> {
        val baseResult = ctx.base.accept(this) ?: ParseResult(Quantity.One, ctx.base.sourceInterval, ErrorMessage("Inferred base in ^", ctx.base.sourceInterval))
        val exponent = if (ctx.exponent.number == null) 1 else parseNumber(ctx.exponent.number.text)
        val sign = if (ctx.exponent.sign == null) 1 else -1

        return baseResult.invoke { base -> base.pow(sign * exponent) }
    }

    override fun visitFunction(ctx: DigitsParser.FunctionContext): ParseResult<Quantity>? {
        val functionNameSyntax = ctx.functionName()
        val functionName = functionNameSyntax.text
        val expressionResult = ctx.argument.accept(this) ?: ParseResult(Quantity.Zero, ctx.sourceInterval, ErrorMessage("Inferred argument in function", ctx.sourceInterval))

        val operation = functions[functionName]
        return if (operation == null) {
            expressionResult.error(ErrorMessage("$functionName is not a function", functionNameSyntax.sourceInterval))
        } else {
            expressionResult.invoke(operation)
        }
    }

    override fun visitParenthesizedExpression(ctx: DigitsParser.ParenthesizedExpressionContext): ParseResult<Quantity>? {
        return ctx.expression().accept(this)
    }

    override fun visitAssignUnit(ctx: DigitsParser.AssignUnitContext): ParseResult<Quantity>? {
        val valueResult = ctx.expression().accept(this)
        val unitResult = parseUnit(ctx.unit().text, ctx.unit().sourceInterval)

        return if (valueResult == null) {
            null // Not sure if this is what I want to return here
        } else if (!valueResult.value.unit.dimensionallyEqual(NaturalUnit())) {
            valueResult.error(ErrorMessage("Two units", ctx.unit().sourceInterval))
        } else {
            valueResult.combine(unitResult, ctx.sourceInterval) { value, unit -> Quantity(value.value, unit) }
        }
    }
}

fun parseUnit(input: String, location: Interval) : ParseResult<NaturalUnit> {
    var unit = UnitSystem.void
    val errors = mutableListOf<ErrorMessage>()
    val tokens = TokenIterator(input, location)

    val doubleUnits = UnitSystem.prefixAbbreviations.keys.intersect(UnitSystem.unitAbbreviations.keys)

    val prefix = tokens.nextLargest(UnitSystem.prefixAbbreviations)
    if (prefix != null) {
        val beginning = input.substring(prefix.abbreviation.length).split("/")[0]
        if (doubleUnits.contains(prefix.abbreviation) && (beginning.isEmpty() || !beginning[0].isLetter())) { // Special case for mili which conflicts with meters
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)

            unit = UnitSystem.unitAbbreviations[prefix.abbreviation]!! * exponent
        } else {
            unit = prefix

            if (!tokens.hasNext()) {
                errors += ErrorMessage("Prefix without unit", location)
            }
        }
    }

    var invert = false
    while (tokens.hasNext()) {
        val newUnit = tokens.nextLargest(UnitSystem.unitAbbreviations)
        if (newUnit != null) {
            val baseUnit = if (invert) -newUnit else newUnit
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)

            unit += baseUnit * exponent
        } else if (tokens.isNext("/")) {
            invert = true
            tokens.consume("/")
        } else {
            tokens.next()
            errors.add(ErrorMessage("Unknown unit:", location)) // TODO fix location
        }
    }

    return ParseResult(unit, location, errors)
}