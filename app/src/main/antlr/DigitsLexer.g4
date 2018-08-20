lexer grammar DigitsLexer;

WS: [ \t\r\n\u000C]+ -> skip;

TIMES: '×' | '*';
DIVIDE: '÷' | '/';

PLUS: '+';
MINUS: '-';

DOT: '.';

LPAREN: '(' | '[' | '{';
RPAREN: ')' | ']' | '}';

Digit
    : [0-9]
    ;

Letter
    : [a-zA-Z$_] // these are the "java letters" below 0x7F
    | ~[\u0000-\u007F\uD800-\uDBFF] // covers all characters above 0x7F which are not a surrogate
    | [\uD800-\uDBFF] [\uDC00-\uDFFF] // covers UTF-16 surrogate pairs encodings for U+10000 to U+10FFFF
    ;