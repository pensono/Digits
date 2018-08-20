// Generated from E:/Code/android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.7
package com.ethanshea.digits;
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
	 * Visit a parse tree produced by {@link DigitsParser#expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExpression(DigitsParser.ExpressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#product_expression}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProduct_expression(DigitsParser.Product_expressionContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(DigitsParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#value}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue(DigitsParser.ValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DigitsParser#unit}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnit(DigitsParser.UnitContext ctx);
}