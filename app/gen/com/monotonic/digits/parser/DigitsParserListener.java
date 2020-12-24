// Generated from F:/Data/Code/Android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.9
package com.monotonic.digits.parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DigitsParser}.
 */
public interface DigitsParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by the {@code ExponentExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExponentExpression(DigitsParser.ExponentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ExponentExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExponentExpression(DigitsParser.ExponentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ProductExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterProductExpression(DigitsParser.ProductExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ProductExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitProductExpression(DigitsParser.ProductExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ValueExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterValueExpression(DigitsParser.ValueExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ValueExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitValueExpression(DigitsParser.ValueExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterUnaryMinus(DigitsParser.UnaryMinusContext ctx);
	/**
	 * Exit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitUnaryMinus(DigitsParser.UnaryMinusContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SumExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSumExpression(DigitsParser.SumExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SumExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSumExpression(DigitsParser.SumExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Alphabetic}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterAlphabetic(DigitsParser.AlphabeticContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Alphabetic}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitAlphabetic(DigitsParser.AlphabeticContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ScientificNotation}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterScientificNotation(DigitsParser.ScientificNotationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ScientificNotation}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitScientificNotation(DigitsParser.ScientificNotationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpression(DigitsParser.ParenthesizedExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpression(DigitsParser.ParenthesizedExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code NumericLiteral}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterNumericLiteral(DigitsParser.NumericLiteralContext ctx);
	/**
	 * Exit a parse tree produced by the {@code NumericLiteral}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitNumericLiteral(DigitsParser.NumericLiteralContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TermExponent}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTermExponent(DigitsParser.TermExponentContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TermExponent}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTermExponent(DigitsParser.TermExponentContext ctx);
	/**
	 * Enter a parse tree produced by {@link DigitsParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(DigitsParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(DigitsParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DigitsParser#unitLiteral}.
	 * @param ctx the parse tree
	 */
	void enterUnitLiteral(DigitsParser.UnitLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#unitLiteral}.
	 * @param ctx the parse tree
	 */
	void exitUnitLiteral(DigitsParser.UnitLiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link DigitsParser#functionName}.
	 * @param ctx the parse tree
	 */
	void enterFunctionName(DigitsParser.FunctionNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#functionName}.
	 * @param ctx the parse tree
	 */
	void exitFunctionName(DigitsParser.FunctionNameContext ctx);
}