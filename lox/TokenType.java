package lox;

/*
 * From my understanding: Tokes are reserved symbols/strings that when broken down into lexemes (the building blocks of a line of code)
 * cannot be used for other purposes when written in code.
 */
enum TokenType {
    // Single character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,

    // Tokens that are either one OR two characters long.
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL, LESS, LESS_EQUAL,

    // Literals. (Types?)
    IDENTIFER, STRING, NUMBER,

    // Keywords. (I/O, Condtionals and Loops)
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

    EOF // End-of-File?
}