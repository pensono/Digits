// Generated from E:/Code/android/Digits/app/src/main/antlr\DigitsParser.g4 by ANTLR 4.7
package com.ethanshea.digits;
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
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TIMES=1, PLUS=2, DOT=3, Digit=4, Letter=5;
	public static final int
		RULE_expression = 0, RULE_product_expression = 1, RULE_literal = 2, RULE_value = 3, 
		RULE_unit = 4;
	public static final String[] ruleNames = {
		"expression", "product_expression", "literal", "value", "unit"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'+'", "'.'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "TIMES", "PLUS", "DOT", "Digit", "Letter"
	};
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
		public List<Product_expressionContext> product_expression() {
			return getRuleContexts(Product_expressionContext.class);
		}
		public Product_expressionContext product_expression(int i) {
			return getRuleContext(Product_expressionContext.class,i);
		}
		public List<TerminalNode> PLUS() { return getTokens(DigitsParser.PLUS); }
		public TerminalNode PLUS(int i) {
			return getToken(DigitsParser.PLUS, i);
		}
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		ExpressionContext _localctx = new ExpressionContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(10);
			product_expression();
			setState(15);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==PLUS) {
				{
				{
				setState(11);
				match(PLUS);
				setState(12);
				product_expression();
				}
				}
				setState(17);
				_errHandler.sync(this);
				_la = _input.LA(1);
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

	public static class Product_expressionContext extends ParserRuleContext {
		public List<LiteralContext> literal() {
			return getRuleContexts(LiteralContext.class);
		}
		public LiteralContext literal(int i) {
			return getRuleContext(LiteralContext.class,i);
		}
		public List<TerminalNode> TIMES() { return getTokens(DigitsParser.TIMES); }
		public TerminalNode TIMES(int i) {
			return getToken(DigitsParser.TIMES, i);
		}
		public Product_expressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_product_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterProduct_expression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitProduct_expression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitProduct_expression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Product_expressionContext product_expression() throws RecognitionException {
		Product_expressionContext _localctx = new Product_expressionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_product_expression);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(18);
			literal();
			setState(21); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(19);
				match(TIMES);
				setState(20);
				literal();
				}
				}
				setState(23); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==TIMES );
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

	public static class LiteralContext extends ParserRuleContext {
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public UnitContext unit() {
			return getRuleContext(UnitContext.class,0);
		}
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(25);
			value();
			setState(27);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==Letter) {
				{
				setState(26);
				unit();
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
		enterRule(_localctx, 6, RULE_value);
		int _la;
		try {
			setState(49);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Digit:
				enterOuterAlt(_localctx, 1);
				{
				setState(30); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(29);
					match(Digit);
					}
					}
					setState(32); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Digit );
				setState(41);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==DOT) {
					{
					setState(34);
					match(DOT);
					setState(38);
					_errHandler.sync(this);
					_la = _input.LA(1);
					while (_la==Digit) {
						{
						{
						setState(35);
						match(Digit);
						}
						}
						setState(40);
						_errHandler.sync(this);
						_la = _input.LA(1);
					}
					}
				}

				}
				break;
			case DOT:
				enterOuterAlt(_localctx, 2);
				{
				setState(43);
				match(DOT);
				setState(45); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(44);
					match(Digit);
					}
					}
					setState(47); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==Digit );
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

	public static class UnitContext extends ParserRuleContext {
		public List<TerminalNode> Letter() { return getTokens(DigitsParser.Letter); }
		public TerminalNode Letter(int i) {
			return getToken(DigitsParser.Letter, i);
		}
		public UnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_unit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).enterUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof DigitsParserListener ) ((DigitsParserListener)listener).exitUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof DigitsParserVisitor ) return ((DigitsParserVisitor<? extends T>)visitor).visitUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final UnitContext unit() throws RecognitionException {
		UnitContext _localctx = new UnitContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_unit);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(52); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(51);
				match(Letter);
				}
				}
				setState(54); 
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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\7;\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\3\2\3\2\3\2\7\2\20\n\2\f\2\16\2\23\13\2\3\3"+
		"\3\3\3\3\6\3\30\n\3\r\3\16\3\31\3\4\3\4\5\4\36\n\4\3\5\6\5!\n\5\r\5\16"+
		"\5\"\3\5\3\5\7\5\'\n\5\f\5\16\5*\13\5\5\5,\n\5\3\5\3\5\6\5\60\n\5\r\5"+
		"\16\5\61\5\5\64\n\5\3\6\6\6\67\n\6\r\6\16\68\3\6\2\2\7\2\4\6\b\n\2\2\2"+
		">\2\f\3\2\2\2\4\24\3\2\2\2\6\33\3\2\2\2\b\63\3\2\2\2\n\66\3\2\2\2\f\21"+
		"\5\4\3\2\r\16\7\4\2\2\16\20\5\4\3\2\17\r\3\2\2\2\20\23\3\2\2\2\21\17\3"+
		"\2\2\2\21\22\3\2\2\2\22\3\3\2\2\2\23\21\3\2\2\2\24\27\5\6\4\2\25\26\7"+
		"\3\2\2\26\30\5\6\4\2\27\25\3\2\2\2\30\31\3\2\2\2\31\27\3\2\2\2\31\32\3"+
		"\2\2\2\32\5\3\2\2\2\33\35\5\b\5\2\34\36\5\n\6\2\35\34\3\2\2\2\35\36\3"+
		"\2\2\2\36\7\3\2\2\2\37!\7\6\2\2 \37\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\"#\3"+
		"\2\2\2#+\3\2\2\2$(\7\5\2\2%\'\7\6\2\2&%\3\2\2\2\'*\3\2\2\2(&\3\2\2\2("+
		")\3\2\2\2),\3\2\2\2*(\3\2\2\2+$\3\2\2\2+,\3\2\2\2,\64\3\2\2\2-/\7\5\2"+
		"\2.\60\7\6\2\2/.\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62\64"+
		"\3\2\2\2\63 \3\2\2\2\63-\3\2\2\2\64\t\3\2\2\2\65\67\7\7\2\2\66\65\3\2"+
		"\2\2\678\3\2\2\28\66\3\2\2\289\3\2\2\29\13\3\2\2\2\13\21\31\35\"(+\61"+
		"\638";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}