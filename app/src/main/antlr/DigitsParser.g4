parser grammar DigitsParser;

options { tokenVocab=DigitsLexer; }

expression
    : MINUS expression # UnaryMinus
    | '+'? value # Literal
    | Letter+ # Constant
    | expression unit # AssignUnit
    | LPAREN expression RPAREN # ParenthesizedExpression
    | functionName LPAREN argument=expression RPAREN # Function
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

// Here so that we can use this as a syntax node for nice things like sourceInterval
functionName
    : Letter+
    ;