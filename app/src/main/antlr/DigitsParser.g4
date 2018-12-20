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
    | base=expression exponent=exponentValue # Exponent // Not totally happy with this rule, but eh it works
    ;

value
    : Digit+ (DOT Digit*)?
    | DOT Digit+
    ;

exponentValue
    : CARET sign=MINUS? number=Digit+
    | sign=SUPERSCRIPT_MINUS? number=Superscript+
    ;

unit
    : Letter (Letter | Digit | Superscript)*
    ;

// Here so that we can use this as a syntax node for nice things like sourceInterval
functionName
    : Letter+
    ;