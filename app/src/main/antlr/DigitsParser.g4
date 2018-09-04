parser grammar DigitsParser;

options { tokenVocab=DigitsLexer; }

expression
    : MINUS expression # UnaryMinus
    | value unit? # Literal
    | LPAREN expression RPAREN # ParenthesizedExpression
    | lhs=expression operation=(TIMES | DIVIDE) rhs=expression # ProductExpression
    | lhs=expression operation=(PLUS | MINUS) rhs=expression # SumExpression
    | base=expression CARET? exponent=Digit+ # Exponent
    ;

value
    : Digit+ (DOT Digit*)?
    | DOT Digit+
    ;

unit
    : Letter+
    ;