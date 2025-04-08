 package com.Parser.Java_Simple_Parser;

 import com.Parser.Java_Simple_Parser.Token;
 import com.Parser.Java_Simple_Parser.TokenType;
 import java.util.List;

 public class Parser {

    private List<Token> tokens;
    private int pos = 0;

    public Parser(){
    }

    public Parser(List<Token> tokens){
      this.tokens = tokens;
    }

    private Token peek() {
      return tokens.get(pos);
    }

    private Token advance() {
         pos += 1;
         if( pos < tokens.size() )
            return tokens.get(pos);

         return null;
    }

    private boolean match(TokenType type) {

        if (pos < tokens.size() && peek().type == type) {
            advance();
            return true;
        }
        return false;
    }

    public void parse() {
        System.out.println("Parsing started.");
        E(); // Start symbol
        System.out.println("Parsing completed successfully.");
    }

    // E → T E'
    private void E() {
        System.out.println("Enter E");
        T();
        EPrime();
        System.out.println("Exit E");
    }

    // E' → + T E' | ε
    private void EPrime() {
        System.out.println("Enter E'");
        if (match(TokenType.PLUS)) {
            System.out.println("Matched '+'");
            T();
            EPrime();
        }
        System.out.println("Exit E'");
    }

    // T → F T'
    private void T() {
        System.out.println("Enter T");
        F();
        TPrime();
        System.out.println("Exit T");
    }

    // T' → * F T' | / F T' | ε
    private void TPrime() {
        System.out.println("Enter T'");
        if (match(TokenType.STAR)) {
            System.out.println("Matched '*'");
            F();
            TPrime();
        } else if (match(TokenType.SLASH)) {
            System.out.println("Matched '/'");
            F();
            TPrime();
        }
        System.out.println("Exit T'");
    }

    private void F() {
        System.out.println("Enter F - " + tokens.get(pos).type);
        if (match(TokenType.STAR)) {
            System.out.println("Matched unary '*'");
            F();
        } else if (match(TokenType.AMP)) {
            System.out.println("Matched '&'");
            F();
        } else if (match(TokenType.LPAREN)) {
            System.out.println("Matched '('");
            E();
            if (!match(TokenType.RPAREN)) {
                throw new RuntimeException("Expected ')'");
            }
            System.out.println("Matched ')'");
        } else if (match(TokenType.ID)) {
            System.out.println("Matched 'id'");
        } else {
            throw new RuntimeException("Unexpected token in F: " + peek());
        }
        System.out.println("Exit F");
   }
}
