parser grammar DigitsParser;

options { tokenVocab=DigitsLexer; }

expression
    : MINUS argument=expression # UnaryMinus
    | lhs=expression operation=(TIMES | DIVIDE) rhs=expression # ProductExpression
    | lhs=expression operation=(PLUS | MINUS) rhs=expression # SumExpression
    | base=expression operator=CARET exponent=expression # ExponentExpression
    | '+'? terms+=term+ # ValueExpression
    ;

// Terms can be juxtaposed, as in 2eπ
term
    : Letter+ # Alphabetic
    | SCI_E sign=MINUS? Digit+ # ScientificNotation
    | LPAREN inner=expression RPAREN # ParenthesizedExpression
    | value # NumericLiteral
    | SUPERSCRIPT_MINUS? Superscript+ # TermExponent
    ;

value
    : Digit+ (DOT Digit*)?
    | DOT Digit+
    ;

unitLiteral
    : (Letter (Digit+ | Superscript+)?)+ (DIVIDE (Letter (Digit+ | Superscript+)?)+)?
    ;

// Here so that we can use this as a syntax node for nice things like sourceInterval
functionName
    : Letter+
    ;