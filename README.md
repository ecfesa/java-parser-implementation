# Java Simple Parser

A web-based implementation of a compiler's parser component that demonstrates tokenization, parsing, and syntax tree generation for a simple expression grammar.

## Overview

This project is a Spring Boot web application that provides an interactive interface for testing and visualizing the parsing of simple expressions. The grammar supports:

- Basic arithmetic operations (+, *, /)
- Nested expressions with parentheses
- C-style operators for addressing (&) and dereferencing (*)
- Variable identifiers

The parser implements a recursive descent algorithm based on the following grammar:

```
E  → T E'
E' → + T E' | ε
T  → F T'
T' → * F T' | / F T' | ε
F  → * F | & F | ( E ) | id
```

## Features

- Interactive web interface to input expressions for parsing
- Visual representation of the syntax tree for valid expressions
- Error reporting for invalid syntax
- Built-in test cases for demonstration
- Support for user-defined test cases

## Technologies Used

- Java 17
- Spring Boot 3.x
- Thymeleaf for server-side templating
- Maven for build management

## Getting Started

### Prerequisites

- JDK 17 or later
- Maven 3.6.x or later

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/ecfesa/java-parser-implementation.git
   cd java-parser-implementation
   ```

2. Build and run the project:
   ```bash
   ./mvnw spring-boot:run
   ```

3. Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

## Usage

1. Enter an expression in the input field (e.g., `a+b*c`, `(a+b)*(c+d)`, `&*a`)
2. Click "Parse" to analyze the expression
3. View the generated syntax tree or error message
4. Use "Clear All" to reset the test cases to defaults

## Project Structure

- `Tokenizer.java`: Converts input strings into tokens
- `Token.java` & `TokenType.java`: Define the token structure
- `Parser.java`: Implements the recursive descent parser and tree generation
- `MainController.java`: Handles HTTP requests and responses
- Web templates in `src/main/resources/templates`

## Example Expressions

Valid examples:
- `a+b` - Simple addition
- `a+b*c` - Expression with operator precedence
- `(a+b)*c` - Expression with parentheses
- `&a` - Address operator
- `*a` - Dereference operator

Invalid examples:
- `a)` - Unmatched right parenthesis
- `(a` - Unmatched left parenthesis
- `a++b` - Double operator
- `*` - Operator without operand
