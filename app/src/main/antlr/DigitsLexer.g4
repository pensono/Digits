lexer grammar DigitsLexer;

WS: [ \t\r\n\u000C]+ -> channel(HIDDEN);

TIMES: '\u00D7' | '*';
DIVIDE: '\u00F7' | '/';

PLUS: '+';
MINUS: '-';

CARET: '^';

DOT: '.';

SCI_E: '\u1D07';

LPAREN: '(' | '[' | '{';
RPAREN: ')' | ']' | '}';

Digit
    : [0-9]
    ;

Superscript
    : [\u2070-\u207a\u207c-\u207f\u00B2\u00B3\u00B9]
    ;

SUPERSCRIPT_MINUS: '\u207B';

Letter
    : [a-zA-Z$_] // these are the "java letters" below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate, or a superscript
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;