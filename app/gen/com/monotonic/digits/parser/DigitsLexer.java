// Generated from F:/Data/Code/Android/Digits/app/src/main/antlr\DigitsLexer.g4 by ANTLR 4.9
package com.monotonic.digits.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DigitsLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, TIMES=2, DIVIDE=3, PLUS=4, MINUS=5, CARET=6, DOT=7, SCI_E=8, LPAREN=9, 
		RPAREN=10, Digit=11, Superscript=12, SUPERSCRIPT_MINUS=13, Letter=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "TIMES", "DIVIDE", "PLUS", "MINUS", "CARET", "DOT", "SCI_E", "LPAREN", 
			"RPAREN", "Digit", "Superscript", "SUPERSCRIPT_MINUS", "Letter"
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


	public DigitsLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DigitsLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20D\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\6\2!\n\2\r\2\16\2\"\3\2\3"+
		"\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13"+
		"\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\17\3\17\5\17C\n\17\2\2\20"+
		"\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20"+
		"\3\2\r\5\2\13\f\16\17\"\"\4\2,,\u00d9\u00d9\4\2\61\61\u00f9\u00f9\5\2"+
		"**]]}}\5\2++__\177\177\3\2\62;\6\2\u00b4\u00b5\u00bb\u00bb\u2072\u207c"+
		"\u207e\u2081\6\2&&C\\aac|\4\2\2\u0081\ud802\udc01\3\2\ud802\udc01\3\2"+
		"\udc02\ue001\2F\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3"+
		"\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2"+
		"\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3 \3\2\2\2\5&\3\2"+
		"\2\2\7(\3\2\2\2\t*\3\2\2\2\13,\3\2\2\2\r.\3\2\2\2\17\60\3\2\2\2\21\62"+
		"\3\2\2\2\23\64\3\2\2\2\25\66\3\2\2\2\278\3\2\2\2\31:\3\2\2\2\33<\3\2\2"+
		"\2\35B\3\2\2\2\37!\t\2\2\2 \37\3\2\2\2!\"\3\2\2\2\" \3\2\2\2\"#\3\2\2"+
		"\2#$\3\2\2\2$%\b\2\2\2%\4\3\2\2\2&\'\t\3\2\2\'\6\3\2\2\2()\t\4\2\2)\b"+
		"\3\2\2\2*+\7-\2\2+\n\3\2\2\2,-\7/\2\2-\f\3\2\2\2./\7`\2\2/\16\3\2\2\2"+
		"\60\61\7\60\2\2\61\20\3\2\2\2\62\63\7\u1d09\2\2\63\22\3\2\2\2\64\65\t"+
		"\5\2\2\65\24\3\2\2\2\66\67\t\6\2\2\67\26\3\2\2\289\t\7\2\29\30\3\2\2\2"+
		":;\t\b\2\2;\32\3\2\2\2<=\7\u207d\2\2=\34\3\2\2\2>C\t\t\2\2?C\n\n\2\2@"+
		"A\t\13\2\2AC\t\f\2\2B>\3\2\2\2B?\3\2\2\2B@\3\2\2\2C\36\3\2\2\2\5\2\"B"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}