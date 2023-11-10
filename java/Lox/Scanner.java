package Lox;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/*
 * The scanner encapsulates a loop, which goes through the source-code line-by-line and first determines which characters belong to the next lexeme.
 * After the lexeme has been identified, all characters belonging to said lexeme are consumed and outputs a token corresponding to the lexeme.
 * This process then repeats for all characters/lexemes in the source-code until it reaches the end of the code.
 */
class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0; // Pointer to the first character of the lexeme being consumed.
    private int current = 0; // Pointer to the character currently being considered by the scanner loop.
    private int line = 1; // Pointer/tracker to the line from source which current is pointing to.

    // Hashmap for Lox keywords.
    private static final Map<String, TokenType> keywords;
    static 
    {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    Scanner(String source)
    {
        this.source = source; // raw source-code.
    }

    // The aformentioned encapsulated loop, which is the primary 'function' of the Scanner class.
    List<Token> scanTokens()
    {
        while (!isAtEnd())
        {
            // At the beginning of the next lexeme in the source.
            start = current;
            scanToken();
        }

        // Final token, to signify that the scanner succesfully reached the end of the source.
        tokens.add(new Token(TokenType.EOF, "", null, line));
        return tokens;
    }

    // Method that scans for tokens from source and outputs matching tokens.
    private void scanToken()
    {
        char c = advance();

        // Single character token match.
        switch (c) 
        {
            case '(': addToken(TokenType.LEFT_PAREN); break;
            case ')': addToken(TokenType.RIGHT_PAREN); break;
            case '{': addToken(TokenType.LEFT_BRACE); break;
            case '}': addToken(TokenType.RIGHT_BRACE); break;
            case ',': addToken(TokenType.COMMA); break;
            case '.': addToken(TokenType.DOT); break;
            case '-': addToken(TokenType.MINUS); break;
            case '+': addToken(TokenType.PLUS); break;
            case ';': addToken(TokenType.SEMICOLON); break;
            case '*': addToken(TokenType.STAR); break;

            // Possible two-character tokens match
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL: TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;

            // Division operator and comment handling
            case '/':
                // Comment handling, consume until line end without appending token.
                if (match('/')) 
                {
                    while (peek() != '\n' && !isAtEnd()) advance();
                } 
                else 
                {
                    addToken(TokenType.SLASH);
                }
                break;
            
            // Characters that don't affect code.
            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;
            
            // New-line handling.
            case '\n':
                line++;
                break;

            // Literals:
            // String handling
            case '"': 
                string(); 
                break;
            
            default:
                if (isDigit(c)) // Numeric literals (floating point AND intergers)
                {
                    number();
                }
                else if (isAlpha(c)) // Identifier handling, assumes all identifiers start with either a letter or an underscore '_'.
                {
                    identifier();
                }
                else // Unsupported character handling.
                { 
                    Lox.error(line, "Unexpected/unhandled character.");
                }
                break;
        }
    }

    // Handles string literal tokens.
    private void string()
    {
        // Loop that consumes characters until next '"' character is encountered or Scanner reaches the end of the source.
        while (peek() != '"' && !isAtEnd())
        {
            if (peek() == '\n') line++; // Multi-line strings.
            advance();
        }

        // If Scanner reaches the end of the source, report error.
        if (isAtEnd())
        {
            Lox.error(line, "Unterminated string.");
            return;
        }

        // The closing '"' character for the string
        advance();

        // Trim away the qutoes from the string.
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    // Handles numeric literal tokens.
    private void number()
    {
        // Handle all pre-decimal numbers
        while (isDigit(peek())) advance();

        // Checks for a decimal part to the numeric literal
        if (peek() == '.' && isDigit(peekNext()))
        {
            // Consume the '.' character.
            advance();
        }

        // Handle all decimal numbers
        while (isDigit(peek())) advance();

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    // Handles the identifier tokens.
    private void identifier()
    {
        while (isAlphaNumeric(peek())) advance();

        // Either match the text to a keyword or pass it along as a identifier token.
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) type = TokenType.IDENTIFER;
        addToken(type);
    }

    // Boolean check on if next character in source matches 'expected'.
    private boolean match(char expected)
    {
        if (isAtEnd()) return false;
        if (source.charAt(current) != expected) return false; // Works as postfix ++ operator returns value and then adds 1.

        current++;
        return true;
    }

    // Peeks at the next character and returns it without consuming (i.e iterating 'current').
    private char peek()
    {
        if (isAtEnd()) return '\0'; // Return end-of-line character.
        return source.charAt(current);
    }

    // Peeks at the character two steps forward from the current character.
    private char peekNext()
    {
        if (current + 1 >= source.length()) return '\0'; // Return end-of-line character.
        return source.charAt(current + 1);
    }

    // Boolean check for use when identifiying if a lexeme corresponds to an identifier token.
    private boolean isAlpha(char c)
    {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
    }

    // Boolean check for identifiers that allow them to contain numeric characters after the initial character in the name.
    private boolean isAlphaNumeric(char c)
    {
        return isAlpha(c) || isDigit(c);
    }

    // Checks if a char 'c' is a digit by ASCII comparision.
    private boolean isDigit(char c)
    {
        return c >= '0' && c <= '9';
    }

    // Helper method to determine if all characters have been consumed or not.
    private boolean isAtEnd()
    {
        return current >= source.length();
    }

    // Advance 'current' pointer and return char at new 'current' pointer position.
    private char advance()
    {
        return source.charAt(current++);
    }

    // TokenType only input method override for addToken
    private void addToken(TokenType type)
    {
        addToken(type, null);
    }

    // Adds a new Token of TokenType 'type' to the 'tokens' list.
    private void addToken(TokenType type, Object literal)
    {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
