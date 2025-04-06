package com.Parser.Java_Simple_Parser;

import java.util.List;

public class ParserExample {
    public static void main(String[] args) {
        // Example expression: a + b * c
        String expression = "a + b * c";

        // Tokenize the expression
        List<Token> tokens = Tokenizer.tokenizer(expression);

        // Print tokens
        System.out.println("Tokens:");
        for (Token token : tokens) {
            System.out.println(token.identification + ": " + token.value);
        }

        // Parse and validate
        Parser parser = new Parser();
        boolean isValid = parser.validate(tokens);

        System.out.println("\nIs the expression valid? " + isValid);

        // Generate parse tree
        if (isValid) {
            try {
                Parser.ParseTreeNode parseTree = parser.parse(tokens);
                System.out.println("\nParse tree generated successfully.");

                // Print parse tree structure (simple representation)
                printParseTree(parseTree, 0);
            } catch (Exception e) {
                System.out.println("Error generating parse tree: " + e.getMessage());
            }
        }

        // Try an invalid expression: a + * b
        System.out.println("\n\nTesting invalid expression: a + * b");
        String invalidExpression = "a + * b";
        List<Token> invalidTokens = Tokenizer.tokenizer(invalidExpression);

        // Print tokens
        System.out.println("Tokens:");
        for (Token token : invalidTokens) {
            System.out.println(token.identification + ": " + token.value);
        }

        // Validate
        boolean isInvalidExpressionValid = parser.validate(invalidTokens);
        System.out.println("\nIs the expression valid? " + isInvalidExpressionValid);
    }

    // Helper method to print the parse tree
    private static void printParseTree(Parser.ParseTreeNode node, int depth) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            indent.append("  ");
        }

        String nodeValue = node.value != null ? node.value : "";
        System.out.println(indent + node.type + " " + nodeValue);

        if (node.children != null) {
            for (Parser.ParseTreeNode child : node.children) {
                printParseTree(child, depth + 1);
            }
        }
    }
}