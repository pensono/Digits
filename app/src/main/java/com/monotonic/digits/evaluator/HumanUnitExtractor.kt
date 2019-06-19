package com.monotonic.digits.evaluator

import com.monotonic.digits.human.HumanUnit
import com.monotonic.digits.human.parseHumanUnit
import com.monotonic.digits.parser.DigitsLexer
import com.monotonic.digits.parser.DigitsParser
import com.monotonic.digits.parser.DigitsParserBaseVisitor
import org.antlr.v4.runtime.misc.Interval

/**
 * @author Ethan
 */

fun usedUnits(input: String) : Set<HumanUnit> = parse(input, UsedUnitsExtractor, setOf()).value

/***
 * @return Map of dimension to the largest prefixed unit which used it
 */
fun largestUsedUnits(input: String) : Map<Map<String, Int>, HumanUnit> {
    val usedUnits = usedUnits(input)
    return usedUnits.groupBy { it.dimensions }
            .mapValues { (_, values) -> values.maxBy { it.exponentMagnitude }!! }
}

object UsedUnitsExtractor : DigitsParserBaseVisitor<ParseResult<Set<HumanUnit>>>() {
    override fun visitAlphabetic(ctx: DigitsParser.AlphabeticContext): ParseResult<Set<HumanUnit>> {
        val unit = parseHumanUnit(ctx.text)
        return ParseResult(listOfNotNull(unit).toSet(), ctx.sourceInterval)
    }

    override fun defaultResult(): ParseResult<Set<HumanUnit>> = ParseResult(setOf(), Interval(0, 0))

    override fun aggregateResult(aggregate: ParseResult<Set<HumanUnit>>, nextResult: ParseResult<Set<HumanUnit>>): ParseResult<Set<HumanUnit>> =
        aggregate.combine(nextResult, aggregate.location.union(nextResult.location)) { agg, next -> agg.union(next) }

    override fun visitProductExpression(ctx: DigitsParser.ProductExpressionContext): ParseResult<Set<HumanUnit>> {
        val rhs = ctx.rhs // Appease the mutability gods
        val lhs = ctx.lhs

        // Special case for "unit/unit"
        return if (ctx.operation.type == DigitsLexer.DIVIDE
                && lhs is DigitsParser.ValueExpressionContext
                && lhs.terms[lhs.terms.size - 1] is DigitsParser.AlphabeticContext
                && rhs is DigitsParser.ValueExpressionContext
                && rhs.terms.size == 1
                && rhs.terms[0] is DigitsParser.AlphabeticContext) {

            val unitText = lhs.terms[lhs.terms.size - 1].text + "/" + rhs.text
            val unit = parseHumanUnit(unitText)

            ParseResult(listOfNotNull(unit).toSet(), ctx.sourceInterval)
        } else {
            aggregateResult(visit(ctx.lhs), visit(ctx.rhs))
        }
    }
}
