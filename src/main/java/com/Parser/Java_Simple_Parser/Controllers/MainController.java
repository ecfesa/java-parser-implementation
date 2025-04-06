package com.Parser.Java_Simple_Parser.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Parser.Java_Simple_Parser.Token;
import com.Parser.Java_Simple_Parser.Tokenizer;
import com.Parser.Java_Simple_Parser.Parser;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home(Model model) {
        List<Map<String, Object>> testResults = new ArrayList<>();

        // Original example
        String input = "Primeiro * Segundo + Terceiro * (Quarto)";
        System.out.println("Testing original expression: " + input);
        Map<String, Object> testResult = runTest(input, "Original expression");
        testResults.add(testResult);

        // Try another valid expression: a + b * c
        String expression = "a + b * c";
        System.out.println("\n\nTesting expression: " + expression);
        testResult = runTest(expression, "Valid expression");
        testResults.add(testResult);

        // Try an invalid expression: a + * b
        String invalidExpression = "a + * b";
        System.out.println("\n\nTesting invalid expression: " + invalidExpression);
        testResult = runTest(invalidExpression, "Invalid expression with consecutive operators");
        testResults.add(testResult);

        // Try an invalid expression with mismatched parentheses: (a + b
        invalidExpression = "(a + b";
        System.out.println("\n\nTesting invalid expression with mismatched parentheses: " + invalidExpression);
        testResult = runTest(invalidExpression, "Invalid expression with mismatched parentheses");
        testResults.add(testResult);

        // Try an invalid expression with double operators: a ++ b
        invalidExpression = "a ++ b";
        System.out.println("\n\nTesting invalid expression with double operators: " + invalidExpression);
        testResult = runTest(invalidExpression, "Invalid expression with double operators");
        testResults.add(testResult);

        // Try an invalid expression with operator at the end: a + b *
        invalidExpression = "a + b *";
        System.out.println("\n\nTesting invalid expression with operator at the end: " + invalidExpression);
        testResult = runTest(invalidExpression, "Invalid expression with operator at the end");
        testResults.add(testResult);

        // Try an invalid expression with missing operand: a * ()
        invalidExpression = "a * ()";
        System.out.println("\n\nTesting invalid expression with missing operand: " + invalidExpression);
        testResult = runTest(invalidExpression, "Invalid expression with missing operand");
        testResults.add(testResult);

        model.addAttribute("testResults", testResults);
        return "index";
    }

    // Method to run a test and gather results
    private Map<String, Object> runTest(String expression, String testName) {
        Map<String, Object> result = new HashMap<>();
        result.put("testName", testName);
        result.put("expression", expression);
        result.put("error", null); // Initialize error field with null

        // Remove spaces for tokenizing if needed
        String processedExpression = expression.replaceAll(" ", "");
        List<Token> tokens = Tokenizer.tokenizer(processedExpression);

        List<Map<String, String>> tokenList = new ArrayList<>();
        for (Token token : tokens) {
            Map<String, String> tokenMap = new HashMap<>();
            tokenMap.put("identification", token.identification);
            tokenMap.put("value", token.value);
            tokenList.add(tokenMap);
        }
        result.put("tokens", tokenList);

        // Parse and validate
        Parser parser = new Parser();
        boolean isValid = parser.validate(tokens);
        result.put("isValid", isValid);

        // Generate parse tree if valid
        if (isValid) {
            try {
                Parser.ParseTreeNode parseTree = parser.parse(tokens);
                result.put("parseTreeHtml", generateParseTreeHtml(parseTree));
            } catch (Exception e) {
                result.put("error", "Error generating parse tree: " + e.getMessage());
            }
        }

        return result;
    }

    // Generate HTML representation of parse tree
    private String generateParseTreeHtml(Parser.ParseTreeNode node) {
        StringBuilder html = new StringBuilder();
        html.append("<ul class=\"tree\">");
        generateTreeNodeHtml(node, html);
        html.append("</ul>");
        return html.toString();
    }

    private void generateTreeNodeHtml(Parser.ParseTreeNode node, StringBuilder html) {
        String nodeValue = node.value != null ? node.value : "";
        html.append("<li>");
        html.append("<span class=\"node-type\">").append(node.type).append("</span>");
        if (!nodeValue.isEmpty()) {
            html.append(" <span class=\"node-value\">").append(nodeValue).append("</span>");
        }

        if (node.children != null && !node.children.isEmpty()) {
            html.append("<ul>");
            for (Parser.ParseTreeNode child : node.children) {
                generateTreeNodeHtml(child, html);
            }
            html.append("</ul>");
        }
        html.append("</li>");
    }

    // Helper method to print the parse tree (kept for console output)
    private void printParseTree(Parser.ParseTreeNode node, int depth) {
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
