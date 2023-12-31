package Lox;

class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line; // Is used for error reporting, to note on which line a token occurs on.

    Token (TokenType type, String lexeme, Object literal, int line) 
    {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String ToString()
    {
        return type + " " + lexeme + literal;
    }
}
