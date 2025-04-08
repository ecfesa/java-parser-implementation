package com.Parser.Java_Simple_Parser;

import com.Parser.Java_Simple_Parser.Token;
import com.Parser.Java_Simple_Parser.TokenType;

import java.util.List;

public class Parser {

    private List<Token> tokens;
    private int pos = 0;

    public static class SyntacticTreeNode {
        public String type;
        public String value;
        public List<SyntacticTreeNode> children;

        public SyntacticTreeNode(String type, String value, List<SyntacticTreeNode> children) {
            this.type = type;
            this.value = value;
            this.children = children;
        }
    }

    public Parser() {
    }

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    private Token advance() {
        pos += 1;
        if (pos < tokens.size())
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

    public SyntacticTreeNode parse() {
        System.out.println("Parsing started.");
        SyntacticTreeNode root = E();

        if (pos < tokens.size()) {
            throw new RuntimeException("Unexpected token in F: " + peek().value);
        }

        System.out.println("Parsing completed successfully.");
        return root;
    }

    // E → T E'
    private SyntacticTreeNode E() {
        System.out.println("Enter E");
        SyntacticTreeNode tNode = T();
        SyntacticTreeNode ePrimeNode = EPrime();
        System.out.println("Exit E");

        List<SyntacticTreeNode> children = new java.util.ArrayList<>();
        children.add(tNode);
        children.add(ePrimeNode);

        return new SyntacticTreeNode("E", null, children);
    }

    // E' → + T E' | ε
    private SyntacticTreeNode EPrime() {
        System.out.println("Enter E'");
        if (match(TokenType.PLUS)) {
            System.out.println("Matched '+'");
            SyntacticTreeNode tNode = T();
            SyntacticTreeNode ePrimeNode = EPrime();

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("PLUS", "+", null));
            children.add(tNode);
            children.add(ePrimeNode);

            return new SyntacticTreeNode("E'", null, children);
        }
        System.out.println("Exit E'");

        // ε production - empty node
        return new SyntacticTreeNode("E'", "ε", null);
    }

    // T → F T'
    private SyntacticTreeNode T() {
        System.out.println("Enter T");
        SyntacticTreeNode fNode = F();
        SyntacticTreeNode tPrimeNode = TPrime();
        System.out.println("Exit T");

        List<SyntacticTreeNode> children = new java.util.ArrayList<>();
        children.add(fNode);
        children.add(tPrimeNode);

        return new SyntacticTreeNode("T", null, children);
    }

    // T' → * F T' | / F T' | ε
    private SyntacticTreeNode TPrime() {
        System.out.println("Enter T'");
        if (match(TokenType.STAR)) {
            System.out.println("Matched '*'");
            SyntacticTreeNode fNode = F();
            SyntacticTreeNode tPrimeNode = TPrime();

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("STAR", "*", null));
            children.add(fNode);
            children.add(tPrimeNode);

            return new SyntacticTreeNode("T'", null, children);
        } else if (match(TokenType.SLASH)) {
            System.out.println("Matched '/'");
            SyntacticTreeNode fNode = F();
            SyntacticTreeNode tPrimeNode = TPrime();

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("SLASH", "/", null));
            children.add(fNode);
            children.add(tPrimeNode);

            return new SyntacticTreeNode("T'", null, children);
        }
        System.out.println("Exit T'");

        // ε production - empty node
        return new SyntacticTreeNode("T'", "ε", null);
    }

    // F → * F | & F | ( E ) | id
    private SyntacticTreeNode F() {
        System.out.println("Enter F - " + tokens.get(pos).type);
        if (match(TokenType.STAR)) {
            System.out.println("Matched unary '*'");
            SyntacticTreeNode fNode = F();

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("STAR", "*", null));
            children.add(fNode);

            return new SyntacticTreeNode("F", null, children);
        } else if (match(TokenType.AMP)) {
            System.out.println("Matched '&'");
            SyntacticTreeNode fNode = F();

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("AMP", "&", null));
            children.add(fNode);

            return new SyntacticTreeNode("F", null, children);
        } else if (match(TokenType.LPAREN)) {
            System.out.println("Matched '('");
            SyntacticTreeNode eNode = E();
            if (!match(TokenType.RPAREN)) {
                throw new RuntimeException("Expected ')'");
            }
            System.out.println("Matched ')'");

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("LPAREN", "(", null));
            children.add(eNode);
            children.add(new SyntacticTreeNode("RPAREN", ")", null));

            return new SyntacticTreeNode("F", null, children);
        } else if (match(TokenType.ID)) {
            System.out.println("Matched 'id'");
            String idValue = tokens.get(pos - 1).value;

            List<SyntacticTreeNode> children = new java.util.ArrayList<>();
            children.add(new SyntacticTreeNode("ID", idValue, null));

            return new SyntacticTreeNode("F", null, children);
        } else {
            throw new RuntimeException("Unexpected token in F: " + peek());
        }
    }
}
