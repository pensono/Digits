// Generated from F:/Data/Code/Android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.8
package com.monotonic.digits.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DigitsParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DigitsParserVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by the {@code ProductExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProductExpression(DigitsParser.ProductExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Exponent}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExponent(DigitsParser.ExponentContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ValueExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValueExpression(DigitsParser.ValueExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code UnaryMinus}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnaryMinus(DigitsParser.UnaryMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SumExpression}
	 * labeled alternative in {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSumExpression(DigitsParser.SumExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Alphabetic}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAlphabetic(DigitsParser.AlphabeticContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ScientificNotation}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitScientificNotation(DigitsParser.ScientificNotationContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ParenthesizedExpression}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParenthesizedExpression(DigitsParser.ParenthesizedExpressionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NumericLiteral}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumericLiteral(DigitsParser.NumericLiteralContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TermExponent}
	 * labeled alternative in {@link DigitsParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermExponent(DigitsParser.TermExponentContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(DigitsParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#unitLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnitLiteral(DigitsParser.UnitLiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#functionName}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFunctionName(DigitsParser.FunctionNameContext ctx);
}