package com.ethshea.digits.evaluator

import com.ethshea.digits.units.NaturalUnit
import com.ethshea.digits.SciNumber
import com.ethshea.digits.units.UnitSystem
import com.ethshea.digits.parser.DigitsLexer
import com.ethshea.digits.parser.DigitsParser
import com.ethshea.digits.parser.DigitsParserBaseVisitor
import com.ethshea.digits.isNumber
import com.ethshea.digits.parseNumber
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ConsoleErrorListener
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.misc.Interval

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val location: Interval)

// TODO Why does this have to be generic?
data class ParseResult<T>(val value: T, val errors: Collection<ErrorMessage> = listOf(), val location: Interval? = null) {
    constructor(value: T, location: Interval? = null, error: ErrorMessage) : this(value, listOf(error), location)
    constructor(value: T, location: Interval, errorMessage: String) : this(value, location, ErrorMessage(errorMessage, location))

    fun <R, A> invoke(argument: A, operation: (T, A) -> R) = ParseResult(operation(value, argument), errors, location)
    fun <R> invoke(operation: (T) -> R) = ParseResult(operation(value), errors, location)

    fun <R, A> combine(argument: ParseResult<A>, operation: (T, A) -> R) = ParseResult(operation(value, argument.value), errors + argument.errors, location)

    fun error(message: ErrorMessage) = ParseResult(value, errors + listOf(message), location)
}

fun evaluateExpression(input: String) : ParseResult<Quantity> {
    val lexer = DigitsLexer(CharStreams.fromString(input))
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE) // Error messages enabled by default -_-
    val tokens = CommonTokenStream(lexer)
    val parser = DigitsParser(tokens)
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE)

    return parser.expression()?.accept(Evaluator) ?: ParseResult(Quantity.Zero)
}

private fun intervalOf(operation: Token): Interval = Interval.of(operation.startIndex, operation.stopIndex)

// A null ParseResult means use whatever the identity element for the operation is
object Evaluator : DigitsParserBaseVisitor<ParseResult<Quantity>?>() {
    private val constants = mapOf(
            "π" to Math.PI,
            "pi" to Math.PI,
            "e" to Math.E
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
        val value = SciNumber(ctx.value().text)

        return ParseResult(Quantity(value))
    }

    override fun visitConstant(ctx: DigitsParser.ConstantContext): ParseResult<Quantity>? {
        val constantName = ctx.text.toLowerCase()

        val constant = constants[constantName]
        return if (constant == null) {
            // Not sure what value to use here
            ParseResult(Quantity.Zero, ctx.sourceInterval, ErrorMessage(ctx.text + " is not a known constant", ctx.sourceInterval))
        } else {
            ParseResult(Quantity(SciNumber(constant)))
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

        return lhsResult.combine(rhsResult, operation)
    }

    override fun visitProductExpression(ctx: DigitsParser.ProductExpressionContext): ParseResult<Quantity> {
        val lhsResult = ctx.lhs.accept(this) ?: ParseResult(Quantity.One, ctx.lhs.sourceInterval, ErrorMessage("Inferred lhs in *", intervalOf(ctx.operation)))
        val rhsResult = ctx.rhs.accept(this) ?: ParseResult(Quantity.One, ctx.rhs.sourceInterval, ErrorMessage("Inferred rhs in *", intervalOf(ctx.operation)))
        val operation = if (ctx.operation.type == DigitsLexer.TIMES) Quantity::times else Quantity::div

        return lhsResult.combine(rhsResult, operation)
    }

    override fun visitExponent(ctx: DigitsParser.ExponentContext): ParseResult<Quantity> {
        val baseResult = ctx.base.accept(this) ?: ParseResult(Quantity.One, ctx.base.sourceInterval, ErrorMessage("Inferred base in ^", intervalOf(ctx.exponent)))
        val exponent = parseNumber(ctx.exponent.text)

        return baseResult.invoke { base -> base.pow(exponent) }
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
            valueResult.combine(unitResult) { value, unit -> Quantity(value.value, unit) }
        }
    }
}

fun parseUnit(input: String, location: Interval) : ParseResult<NaturalUnit> {
    var invert = false
    var unit = UnitSystem.void
    val errors = mutableListOf<ErrorMessage>()
    val tokens = TokenIterator(input, location)

    val prefix = tokens.nextLargest(UnitSystem.prefixAbbreviations)
    if (prefix != null) {
        val beginning = input.substring(prefix.abbreviation.length).split("/")[0]
        if (prefix.abbreviation == "m" && (beginning.isEmpty() || !beginning[0].isLetter())) { // Special case for mili which conflicts with meters
            val exponentStr = tokens.nextWhile(::isNumber)
            val exponent = if (exponentStr == "") 1 else parseNumber(exponentStr)

            unit = UnitSystem.unitAbbreviations["m"]!! * exponent
        } else {
            unit = prefix
        }
    }

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

    return ParseResult(unit, errors, location)
}