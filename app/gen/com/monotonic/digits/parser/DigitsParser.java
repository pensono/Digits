// Generated from F:/Data/Code/Android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.9
package com.monotonic.digits.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DigitsParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, TIMES=2, DIVIDE=3, PLUS=4, MINUS=5, CARET=6, DOT=7, SCI_E=8, LPAREN=9, 
		RPAREN=10, Digit=11, Superscript=12, SUPERSCRIPT_MINUS=13, Letter=14;
	public static final int
		RULE_expression = 0, RULE_term = 1, RULE_value = 2, RULE_unitLiteral = 3, 
		RULE_functionName = 4;
	private static String[] makeRuleNames() {
		return new String[] {
			"expression", "term", "value", "unitLiteral", "functionName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, "'+'", "'-'", "'^'", "'.'", "'\u1D07'", null, 
			null, null, null, "'\u207B'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "TIMES", "DIVIDE", "PLUS", "MINUS", "CARET", "DOT", "SCI_E", 
			"LPAREN", "RPAREN", "Digit", "Superscript", "SUPERSCRIPT_MINUS", "Letter"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "DigitsParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public DigitsParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ExpressionContext extends ParserRuleContext {
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
	 
		public ExpressionContext() { }
		public void copyFrom(ExpressionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExponentExpressionContext extends ExpressionContext {
		public ExpressionContext base;
		public Token operator;
		public ExpressionContext exponent;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode CARET() { return getToken(DigitsParser.CARET, 0); }
		public ExponentExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterExponentExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitExponentExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitExponentExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ProductExpressionContext extends ExpressionContext {
		public ExpressionContext lhs;
		public Token operation;
		public ExpressionContext rhs;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode TIMES() { return getToken(DigitsParser.TIMES, 0); }
		public TerminalNode DIVIDE() { return getToken(DigitsParser.DIVIDE, 0); }
		public ProductExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterProductExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitProductExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitProductExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ValueExpressionContext extends ExpressionContext {
		public TermContext term;
		public List<TermContext> terms = new ArrayList<TermContext>();
		public TerminalNode PLUS() { return getToken(DigitsParser.PLUS, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public ValueExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterValueExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitValueExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitValueExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class UnaryMinusContext extends ExpressionContext {
		public ExpressionContext argument;
		public TerminalNode MINUS() { return getToken(DigitsParser.MINUS, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public UnaryMinusContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterUnaryMinus(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitUnaryMinus(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitUnaryMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SumExpressionContext extends ExpressionContext {
		public ExpressionContext lhs;
		public Token operation;
		public ExpressionContext rhs;
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(DigitsParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(DigitsParser.MINUS, 0); }
		public SumExpressionContext(ExpressionContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterSumExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitSumExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitSumExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 0;
		enterRecursionRule(_localctx, 0, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(21);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case MINUS:
				{
				_localctx = new UnaryMinusContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(11);
				match(MINUS);
				setState(12);
				((UnaryMinusContext)_localctx).argument = expression(5);
				}
				break;
			case PLUS:
			case DOT:
			case SCI_E:
			case LPAREN:
			case Digit:
			case Superscript:
			case SUPERSCRIPT_MINUS:
			case Letter:
				{
				_localctx = new ValueExpressionContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(14);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==PLUS) {
					{
					setState(13);
					match(PLUS);
					}
				}

				setState(17); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(16);
						((ValueExpressionContext)_localctx).term = term();
						((ValueExpressionContext)_localctx).terms.add(((ValueExpressionContext)_localctx).term);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(19); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,1,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(34);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(32);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
					case 1:
						{
						_localctx = new ExponentExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((ExponentExpressionContext)_localctx).base = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(23);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(24);
						((ExponentExpressionContext)_localctx).operator = match(CARET);
						setState(25);
						((ExponentExpressionContext)_localctx).exponent = expression(5);
						}
						break;
					case 2:
						{
						_localctx = new ProductExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((ProductExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(26);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(27);
						((ProductExpressionContext)_localctx).operation = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==TIMES || _la==DIVIDE) ) {
							((ProductExpressionContext)_localctx).operation = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(28);
						((ProductExpressionContext)_localctx).rhs = expression(4);
						}
						break;
					case 3:
						{
						_localctx = new SumExpressionContext(new ExpressionContext(_parentctx, _parentState));
						((SumExpressionContext)_localctx).lhs = _prevctx;
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(29);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(30);
						((SumExpressionContext)_localctx).operation = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((SumExpressionContext)_localctx).operation = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(31);
						((SumExpressionContext)_localctx).rhs = expression(3);
						}
						break;
					}
					} 
				}
				setState(36);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ParenthesizedExpressionContext extends TermContext {
		public ExpressionContext inner;
		public TerminalNode LPAREN() { return getToken(DigitsParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(DigitsParser.RPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ParenthesizedExpressionContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterParenthesizedExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitParenthesizedExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitParenthesizedExpression(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NumericLiteralContext extends TermContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public NumericLiteralContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterNumericLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitNumericLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitNumericLiteral(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ScientificNotationContext extends TermContext {
		public Token sign;
		public TerminalNode SCI_E() { return getToken(DigitsParser.SCI_E, 0); }
		public List<TerminalNode> Digit() { return getTokens(DigitsParser.Digit); }
		public TerminalNode Digit(int i) {
			return getToken(DigitsParser.Digit, i);
		}
		public TerminalNode MINUS() { return getToken(DigitsParser.MINUS, 0); }
		public ScientificNotationContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterScientificNotation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitScientificNotation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitScientificNotation(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AlphabeticContext extends TermContext {
		public List<TerminalNode> Letter() { return getTokens(DigitsParser.Letter); }
		public TerminalNode Letter(int i) {
			return getToken(DigitsParser.Letter, i);
		}
		public AlphabeticContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterAlphabetic(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitAlphabetic(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitAlphabetic(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermExponentContext extends TermContext {
		public TerminalNode SUPERSCRIPT_MINUS() { return getToken(DigitsParser.SUPERSCRIPT_MINUS, 0); }
		public List<TerminalNode> Superscript() { return getTokens(DigitsParser.Superscript); }
		public TerminalNode Superscript(int i) {
			return getToken(DigitsParser.Superscript, i);
		}
		public TermExponentContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterTermExponent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitTermExponent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitTermExponent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_term);
		int _la;
		try {
			int _alt;
			setState(64);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Letter:
				_localctx = new AlphabeticContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(38); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(37);
						match(Letter);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(40); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case SCI_E:
				_localctx = new ScientificNotationContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(42);
				match(SCI_E);
				setState(44);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==MINUS) {
					{
					setState(43);
					((ScientificNotationContext)_localctx).sign = match(MINUS);
					}
				}

				setState(47); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(46);
						match(Digit);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(49); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			case LPAREN:
				_localctx = new ParenthesizedExpressionContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(51);
				match(LPAREN);
				setState(52);
				((ParenthesizedExpressionContext)_localctx).inner = expression(0);
				setState(53);
				match(RPAREN);
				}
				break;
			case DOT:
			case Digit:
				_localctx = new NumericLiteralContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(55);
				value();
				}
				break;
			case Superscript:
			case SUPERSCRIPT_MINUS:
				_localctx = new TermExponentContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(57);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==SUPERSCRIPT_MINUS) {
					{
					setState(56);
					match(SUPERSCRIPT_MINUS);
					}
				}

				setState(60); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(59);
						match(Superscript);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(62); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public List<TerminalNode> Digit() { return getTokens(DigitsParser.Digit); }
		public TerminalNode Digit(int i) {
			return getToken(DigitsParser.Digit, i);
		}
		public TerminalNode DOT() { return getToken(DigitsParser.DOT, 0); }
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_value);
		try {
			int _alt;
			setState(86);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Digit:
				enterOuterAlt(_localctx, 1);
				{
				setState(67); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(66);
						match(Digit);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(69); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				setState(78);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
				case 1:
					{
					setState(71);
					match(DOT);
					setState(75);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
					while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
						if ( _alt==1 ) {
							{
							{
							setState(72);
							match(Digit);
							}
							} 
						}
						setState(77);
						_errHandler.sync(this);
						_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
					}
					}
					break;
				}
				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(80);
				match(DOT);
				setState(82); 
				_errHandler.sync(this);
				_alt = 1;
				do {
					switch (_alt) {
					case 1:
						{
						{
						setState(81);
						match(Digit);
						}
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					setState(84); 
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
				} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class UnitLiteralContext extends ParserRuleContext {
		public List<TerminalNode> Letter() { return getTokens(DigitsParser.Letter); }
		public TerminalNode Letter(int i) {
			return getToken(DigitsParser.Letter, i);
		}
		public TerminalNode DIVIDE() { return getToken(DigitsParser.DIVIDE, 0); }
		public List<TerminalNode> Digit() { return getTokens(DigitsParser.Digit); }
		public TerminalNode Digit(int i) {
			return getToken(DigitsParser.Digit, i);
		}
		public List<TerminalNode> Superscript() { return getTokens(DigitsParser.Superscript); }
		public TerminalNode Superscript(int i) {
			return getToken(DigitsParser.Superscript, i);
		}
		public UnitLiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unitLiteral; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterUnitLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitUnitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitUnitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitLiteralContext unitLiteral() throws RecognitionException {
		UnitLiteralContext _localctx = new UnitLiteralContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_unitLiteral);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(88);
				match(Letter);
				setState(99);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case Digit:
					{
					setState(90); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(89);
						match(Digit);
						}
						}
						setState(92); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==Digit );
					}
					break;
				case Superscript:
					{
					setState(95); 
					_errHandler.sync(this);
					_la = _input.LA(1);
					do {
						{
						{
						setState(94);
						match(Superscript);
						}
						}
						setState(97); 
						_errHandler.sync(this);
						_la = _input.LA(1);
					} while ( _la==Superscript );
					}
					break;
				case EOF:
				case DIVIDE:
				case Letter:
					break;
				default:
					break;
				}
				}
				}
				setState(103); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Letter );
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==DIVIDE) {
				{
				setState(105);
				match(DIVIDE);
				setState(119); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(106);
					match(Letter);
					setState(117);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case Digit:
						{
						setState(108); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(107);
							match(Digit);
							}
							}
							setState(110); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==Digit );
						}
						break;
					case Superscript:
						{
						setState(113); 
						_errHandler.sync(this);
						_la = _input.LA(1);
						do {
							{
							{
							setState(112);
							match(Superscript);
							}
							}
							setState(115); 
							_errHandler.sync(this);
							_la = _input.LA(1);
						} while ( _la==Superscript );
						}
						break;
					case EOF:
					case Letter:
						break;
					default:
						break;
					}
					}
					}
					setState(121); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Letter );
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FunctionNameContext extends ParserRuleContext {
		public List<TerminalNode> Letter() { return getTokens(DigitsParser.Letter); }
		public TerminalNode Letter(int i) {
			return getToken(DigitsParser.Letter, i);
		}
		public FunctionNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_functionName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterFunctionName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitFunctionName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitFunctionName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FunctionNameContext functionName() throws RecognitionException {
		FunctionNameContext _localctx = new FunctionNameContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_functionName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(125);
				match(Letter);
				}
				}
				setState(128); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==Letter );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 0:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 4);
		case 1:
			return precpred(_ctx, 3);
		case 2:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\20\u0085\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\3\2\5\2\21\n\2\3\2\6\2\24"+
		"\n\2\r\2\16\2\25\5\2\30\n\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\7\2#\n"+
		"\2\f\2\16\2&\13\2\3\3\6\3)\n\3\r\3\16\3*\3\3\3\3\5\3/\n\3\3\3\6\3\62\n"+
		"\3\r\3\16\3\63\3\3\3\3\3\3\3\3\3\3\3\3\5\3<\n\3\3\3\6\3?\n\3\r\3\16\3"+
		"@\5\3C\n\3\3\4\6\4F\n\4\r\4\16\4G\3\4\3\4\7\4L\n\4\f\4\16\4O\13\4\5\4"+
		"Q\n\4\3\4\3\4\6\4U\n\4\r\4\16\4V\5\4Y\n\4\3\5\3\5\6\5]\n\5\r\5\16\5^\3"+
		"\5\6\5b\n\5\r\5\16\5c\5\5f\n\5\6\5h\n\5\r\5\16\5i\3\5\3\5\3\5\6\5o\n\5"+
		"\r\5\16\5p\3\5\6\5t\n\5\r\5\16\5u\5\5x\n\5\6\5z\n\5\r\5\16\5{\5\5~\n\5"+
		"\3\6\6\6\u0081\n\6\r\6\16\6\u0082\3\6\2\3\2\7\2\4\6\b\n\2\4\3\2\4\5\3"+
		"\2\6\7\2\u009f\2\27\3\2\2\2\4B\3\2\2\2\6X\3\2\2\2\bg\3\2\2\2\n\u0080\3"+
		"\2\2\2\f\r\b\2\1\2\r\16\7\7\2\2\16\30\5\2\2\7\17\21\7\6\2\2\20\17\3\2"+
		"\2\2\20\21\3\2\2\2\21\23\3\2\2\2\22\24\5\4\3\2\23\22\3\2\2\2\24\25\3\2"+
		"\2\2\25\23\3\2\2\2\25\26\3\2\2\2\26\30\3\2\2\2\27\f\3\2\2\2\27\20\3\2"+
		"\2\2\30$\3\2\2\2\31\32\f\6\2\2\32\33\7\b\2\2\33#\5\2\2\7\34\35\f\5\2\2"+
		"\35\36\t\2\2\2\36#\5\2\2\6\37 \f\4\2\2 !\t\3\2\2!#\5\2\2\5\"\31\3\2\2"+
		"\2\"\34\3\2\2\2\"\37\3\2\2\2#&\3\2\2\2$\"\3\2\2\2$%\3\2\2\2%\3\3\2\2\2"+
		"&$\3\2\2\2\')\7\20\2\2(\'\3\2\2\2)*\3\2\2\2*(\3\2\2\2*+\3\2\2\2+C\3\2"+
		"\2\2,.\7\n\2\2-/\7\7\2\2.-\3\2\2\2./\3\2\2\2/\61\3\2\2\2\60\62\7\r\2\2"+
		"\61\60\3\2\2\2\62\63\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64C\3\2\2\2\65"+
		"\66\7\13\2\2\66\67\5\2\2\2\678\7\f\2\28C\3\2\2\29C\5\6\4\2:<\7\17\2\2"+
		";:\3\2\2\2;<\3\2\2\2<>\3\2\2\2=?\7\16\2\2>=\3\2\2\2?@\3\2\2\2@>\3\2\2"+
		"\2@A\3\2\2\2AC\3\2\2\2B(\3\2\2\2B,\3\2\2\2B\65\3\2\2\2B9\3\2\2\2B;\3\2"+
		"\2\2C\5\3\2\2\2DF\7\r\2\2ED\3\2\2\2FG\3\2\2\2GE\3\2\2\2GH\3\2\2\2HP\3"+
		"\2\2\2IM\7\t\2\2JL\7\r\2\2KJ\3\2\2\2LO\3\2\2\2MK\3\2\2\2MN\3\2\2\2NQ\3"+
		"\2\2\2OM\3\2\2\2PI\3\2\2\2PQ\3\2\2\2QY\3\2\2\2RT\7\t\2\2SU\7\r\2\2TS\3"+
		"\2\2\2UV\3\2\2\2VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XE\3\2\2\2XR\3\2\2\2Y\7"+
		"\3\2\2\2Ze\7\20\2\2[]\7\r\2\2\\[\3\2\2\2]^\3\2\2\2^\\\3\2\2\2^_\3\2\2"+
		"\2_f\3\2\2\2`b\7\16\2\2a`\3\2\2\2bc\3\2\2\2ca\3\2\2\2cd\3\2\2\2df\3\2"+
		"\2\2e\\\3\2\2\2ea\3\2\2\2ef\3\2\2\2fh\3\2\2\2gZ\3\2\2\2hi\3\2\2\2ig\3"+
		"\2\2\2ij\3\2\2\2j}\3\2\2\2ky\7\5\2\2lw\7\20\2\2mo\7\r\2\2nm\3\2\2\2op"+
		"\3\2\2\2pn\3\2\2\2pq\3\2\2\2qx\3\2\2\2rt\7\16\2\2sr\3\2\2\2tu\3\2\2\2"+
		"us\3\2\2\2uv\3\2\2\2vx\3\2\2\2wn\3\2\2\2ws\3\2\2\2wx\3\2\2\2xz\3\2\2\2"+
		"yl\3\2\2\2z{\3\2\2\2{y\3\2\2\2{|\3\2\2\2|~\3\2\2\2}k\3\2\2\2}~\3\2\2\2"+
		"~\t\3\2\2\2\177\u0081\7\20\2\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\13\3\2\2\2\34\20\25\27\"$*.\63"+
		";@BGMPVX^ceipuw{}\u0082";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}