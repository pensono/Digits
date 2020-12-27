package com.monotonic.digits.evaluator

import com.monotonic.digits.parser.DigitsLexer
import com.monotonic.digits.parser.DigitsParser
import com.monotonic.digits.parser.DigitsParserBaseVisitor
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.misc.Interval

/**
 * @author Ethan
 */

data class ErrorMessage(val message: String, val location: Interval)

data class ParseResult<T>(val value: T, val location: Interval, val errors: Collection<ErrorMessage> = listOf()) {
    constructor(value: T, location: Interval, error: ErrorMessage) : this(value, location, listOf(error))
    constructor(value: T, location: Interval, errorMessage: String) : this(value, location, ErrorMessage(errorMessage, location))

    fun <R, A> invoke(argument: A, operation: (T, A) -> R) = ParseResult(operation(value, argument), location, errors)
    fun <R> invoke(operation: (T) -> R) = ParseResult(operation(value), location, errors)

    fun <R, A> combine(argument: ParseResult<A>, newLocation: Interval, operation: (T, A) -> R) = ParseResult(operation(value, argument.value), newLocation, errors + argument.errors)

    fun error(message: ErrorMessage) = ParseResult(value, location, errors + listOf(message))
    fun error(newErrors: Collection<ErrorMessage>) = ParseResult(value, location, errors + newErrors)
}

/**
 * @param defaultValue Value to be returned in the parse result in the case of catastrophic
 *      syntax errors (or an empty input)
 */
fun <T> parse(input: String, visitor: DigitsParserBaseVisitor<ParseResult<T>>, defaultValue: T): ParseResult<T> {
    val lexer = DigitsLexer(CharStreams.fromString(input))
    lexer.removeErrorListener(ConsoleErrorListener.INSTANCE) // Error messages enabled by default -_-
    val tokens = CommonTokenStream(lexer)
    val parser = DigitsParser(tokens)
    parser.removeErrorListener(ConsoleErrorListener.INSTANCE)

    val syntaxErrors = mutableListOf<ErrorMessage>()
    parser.addErrorListener(object : BaseErrorListener() {
        override fun syntaxError(recognizer: Recognizer<*, *>, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String, e: RecognitionException?) {
            syntaxErrors += ErrorMessage(msg, intervalOf(offendingSymbol as Token))
        }
    })

    val expression = parser.expression()
    val result = expression?.accept(visitor) ?: ParseResult(defaultValue, Interval(0, 0))

    if (expression.sourceInterval != Interval(0, input.length - 1)) {
        syntaxErrors += ErrorMessage("Incomplete parse", Interval(expression.sourceInterval.b + 1, input.length - 1))
    }

    return result.error(syntaxErrors)
}


fun intervalOf(operation: Token): Interval = Interval.of(operation.startIndex, operation.stopIndex)