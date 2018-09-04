parser grammar DigitsParser;

options { tokenVocab=DigitsLexer; }

expression
    : MINUS expression # UnaryMinus
    | value # Literal
    | expression unit # AssignUnit
    | LPAREN expression RPAREN # ParenthesizedExpression
    | lhs=expression operation=(TIMES | DIVIDE) rhs=expression # ProductExpression
    | lhs=expression operation=(PLUS | MINUS) rhs=expression # SumExpression
    | base=expression (exponent=Superscript+ | CARET exponent=Digit+) # Exponent
    ;

value
    : Digit+ (DOT Digit*)?
    | DOT Digit+
    ;

unit
    : Letter (Letter | Digit | Superscript)*
    ;