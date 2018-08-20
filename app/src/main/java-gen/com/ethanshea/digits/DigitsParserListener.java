// Generated from E:/Code/android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.7
package com.ethanshea.digits;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DigitsParser}.
 */
public interface DigitsParserListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterExpression(DigitsParser.ExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitExpression(DigitsParser.ExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DigitsParser#product_expression}.
	 * @param ctx the parse tree
	 */
	void enterProduct_expression(DigitsParser.Product_expressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#product_expression}.
	 * @param ctx the parse tree
	 */
	void exitProduct_expression(DigitsParser.Product_expressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link DigitsParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(DigitsParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(DigitsParser.LiteralContext ctx);
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
	 * Enter a parse tree produced by {@link DigitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void enterUnit(DigitsParser.UnitContext ctx);
	/**
	 * Exit a parse tree produced by {@link DigitsParser#unit}.
	 * @param ctx the parse tree
	 */
	void exitUnit(DigitsParser.UnitContext ctx);
}