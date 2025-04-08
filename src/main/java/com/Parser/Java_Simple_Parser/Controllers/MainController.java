package com.Parser.Java_Simple_Parser.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.Parser.Java_Simple_Parser.Token;
import com.Parser.Java_Simple_Parser.Tokenizer;
import com.Parser.Java_Simple_Parser.Parser;
import com.Parser.Java_Simple_Parser.Parser.SyntacticTreeNode;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    private List<TestResult> testResults = new ArrayList<>();
    private boolean initialTestsAdded = false;

    public static class TestResult {
        private String input;
        private boolean passed;
        private String syntaxTree;
        private String errorMessage;
        private boolean builtIn; // Flag to mark built-in examples

        public TestResult(String input, boolean passed, String syntaxTree, String errorMessage, boolean builtIn) {
            this.input = input;
            this.passed = passed;
            this.syntaxTree = syntaxTree;
            this.errorMessage = errorMessage;
            this.builtIn = builtIn;
        }

        public String getInput() {
            return input;
        }

        public boolean isPassed() {
            return passed;
        }

        public String getSyntaxTree() {
            return syntaxTree;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public boolean isBuiltIn() {
            return builtIn;
        }
    }

    @GetMapping("/")
    public String home(Model model) {
        if (!initialTestsAdded) {
            initializeTestCases();
            initialTestsAdded = true;
        }

        model.addAttribute("testResults", testResults);
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }

    @PostMapping("/test")
    public String test(@RequestParam("input") String input, Model model) {
        TestResult result = runTest(input, false); // Mark as user-added
        testResults.add(0, result); // Add to the top of the list

        return "redirect:/";
    }

    @PostMapping("/clear")
    public String clearAll(Model model) {
        testResults.clear();
        initializeTestCases(); // Reinitialize with sample tests

        return "redirect:/";
    }

    private void initializeTestCases() {
        // Valid test cases that demonstrate different grammar elements
        addBuiltInTest("a"); // Simple identifier
        addBuiltInTest("a+b"); // Addition operation
        addBuiltInTest("a+b*c"); // Precedence of operations
        addBuiltInTest("(a+b)*c"); // Grouping with parentheses
        addBuiltInTest("&a"); // Address operator
        addBuiltInTest("*a"); // Dereference operator
        addBuiltInTest("&*a"); // Combined unary operators
        addBuiltInTest("a+b+c+d"); // Multiple additions
        addBuiltInTest("a*b/c*d"); // Mixed multiplication and division
        addBuiltInTest("(a+(b*c))/d"); // Complex nested expression
        addBuiltInTest("((a+b)*(c+d))"); // Complex nested expression with multiple groupings

        // Invalid test cases that demonstrate different error scenarios
        addBuiltInTest("a)"); // Unmatched right parenthesis
        addBuiltInTest("(a"); // Unmatched left parenthesis
        addBuiltInTest("a++b"); // Double operator
        addBuiltInTest("*"); // Operator without operand
        addBuiltInTest("+a"); // Invalid unary operator
        addBuiltInTest("()"); // Empty parentheses
        addBuiltInTest("a+"); // Missing right operand
        addBuiltInTest("a*/b"); // Invalid sequence of operators
    }

    private void addBuiltInTest(String input) {
        TestResult result = runTest(input, true); // Mark as built-in
        testResults.add(result);
    }

    private TestResult runTest(String input, boolean builtIn) {
        input = input.replaceAll(" ", "");

        try {
            List<Token> tokens = Tokenizer.tokenizer(input);
            Parser parser = new Parser(tokens);

            SyntacticTreeNode parseTree = parser.parse();
            String treeString = generateASCIITree(parseTree, 0);

            return new TestResult(input, true, treeString, null, builtIn);
        } catch (Exception e) {
            return new TestResult(input, false, null, e.getMessage(), builtIn);
        }
    }

    private String generateASCIITree(SyntacticTreeNode node, int indent) {
        StringBuilder sb = new StringBuilder();

        if (node == null) {
            return sb.toString();
        }

        if (indent > 0) {
            for (int i = 0; i < indent - 1; i++) {
                sb.append("│  ");
            }
            sb.append("├─ ");
        }

        // Node representation
        sb.append(node.type);
        if (node.value != null && !node.value.equals("ε")) {
            sb.append(": ").append(node.value);
        }
        sb.append("\n");

        // Children representation
        if (node.children != null && !node.children.isEmpty()) {
            for (int i = 0; i < node.children.size(); i++) {
                SyntacticTreeNode child = node.children.get(i);
                sb.append(generateASCIITree(child, indent + 1));
            }
        }

        return sb.toString();
    }
}
