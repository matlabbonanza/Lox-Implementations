package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


// Based on textbook Crafting Interpreters
// Source: https://craftinginterpreters.com/
// Exit codes according to https://man.freebsd.org/cgi/man.cgi?query=sysexits&apropos=0&sektion=0&manpath=FreeBSD+4.3-RELEASE&format=html

public class Lox 
{
    static boolean hadError = false;
    public static void main(String[] args) throws IOException 
    {
        if (args.length > 1) 
        {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }
        else if (args.length == 1) 
        {
            runFile(args[0]); // executes the Lox code given by the file
        }
        else 
        {
            runPrompt(); // interactively run Lox code
        }
    }

    private static void runFile(String path) throws IOException 
    {
        byte[] bytes = Files.readAllBytes(Paths.get(path)); // read raw bytes from file given by `path`
        run(new String(bytes, Charset.defaultCharset())); // convert raw bytes to UTF-8 and run String as Lox code

        // Indicate an error in the exit code.
        if (hadError) System.exit(65);
    }

    private static void runPrompt() throws IOException 
    {
        /*
        * Creates an interactive prompt/REPL, allowing the linewise execution of Lox code.
        */
        InputStreamReader input = new InputStreamReader(System.in); // reads bytes cast to UTF-8 from user input
        BufferedReader reader = new BufferedReader(input); // uses a buffer for more efficent conversions and less method calls

        // Equivalent to a while loop - except for the lack of conditional, making it more efficent(?) 
        // as statement does not have to be checked each iteration
        for (;;)
        {
            System.out.print("> ");
            String line = reader.readLine(); // read an enitre line from `input`
            if (line == null) break; // done by entering CTRL+D(?)
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) 
    {
        /*
         * The core function of the Lox language-
         * Generates `tokens` from the `source` using the `Scanner`.
         */

         Scanner scanner = new Scanner(source);
         List<Token> tokens = scanner.scanTokens();

         // Currently only prints the found tokens.
         for (Token token : tokens) 
         {
            System.out.print(token);
         }
    } 

    // Start:  ----Error Handling----

    static void error(int line, String message) 
    {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) 
    {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message
        );
        hadError = true;
    }
}