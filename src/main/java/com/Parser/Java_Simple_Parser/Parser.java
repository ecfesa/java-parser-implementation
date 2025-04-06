package com.Parser.Java_Simple_Parser;

import java.util.List;

public class Parser {
   private List<Token> tokens;
   private int currentTokenIndex;
   private Token currentToken;

   public Parser() {
   }

   // Parse tree node class to build the syntax tree
   public static class ParseTreeNode {
      public String type; // Non-terminal or terminal type
      public String value; // Value for terminals
      public List<ParseTreeNode> children;

      public ParseTreeNode(String type, String value, List<ParseTreeNode> children) {
         this.type = type;
         this.value = value;
         this.children = children;
      }
   }

   /**
    * Validates a list of tokens against the grammar.
    * 
    * Grammar (after eliminating left recursion):
    * E → T E'
    * E' → + T E' | ε
    * T → F T'
    * T' → * F T' | / F T' | ε
    * F → * F | & F | ( E ) | id
    * 
    * @param tokens List of tokens to validate
    * @return true if the tokens follow the grammar, false otherwise
    */
   public boolean validate(List<Token> tokens) {
      this.tokens = tokens;
      this.currentTokenIndex = 0;

      if (tokens.isEmpty()) {
         return false;
      }

      currentToken = tokens.get(currentTokenIndex);

      try {
         ParseTreeNode root = parseE();
         // Check if we consumed all tokens
         return currentTokenIndex == tokens.size();
      } catch (Exception e) {
         return false;
      }
   }

   private void advance() {
      currentTokenIndex++;
      if (currentTokenIndex < tokens.size()) {
         currentToken = tokens.get(currentTokenIndex);
      }
   }

   private boolean match(String tokenType) {
      if (currentTokenIndex < tokens.size() && currentToken.identification.equals(tokenType)) {
         advance();
         return true;
      }
      return false;
   }

   // E → T E'
   private ParseTreeNode parseE() {
      ParseTreeNode t = parseT();
      ParseTreeNode ePrime = parseEPrime();

      List<ParseTreeNode> children = new java.util.ArrayList<>();
      children.add(t);
      children.add(ePrime);

      return new ParseTreeNode("E", null, children);
   }

   // E' → + T E' | ε
   private ParseTreeNode parseEPrime() {
      if (currentTokenIndex < tokens.size() && currentToken.identification.equals("PLUS")) {
         Token plusToken = currentToken;
         advance();

         ParseTreeNode t = parseT();
         ParseTreeNode ePrime = parseEPrime();

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("PLUS", "+", null));
         children.add(t);
         children.add(ePrime);

         return new ParseTreeNode("E'", null, children);
      }

      // ε production - empty node
      return new ParseTreeNode("E'", "ε", null);
   }

   // T → F T'
   private ParseTreeNode parseT() {
      ParseTreeNode f = parseF();
      ParseTreeNode tPrime = parseTPrime();

      List<ParseTreeNode> children = new java.util.ArrayList<>();
      children.add(f);
      children.add(tPrime);

      return new ParseTreeNode("T", null, children);
   }

   // T' → * F T' | / F T' | ε
   private ParseTreeNode parseTPrime() {
      if (currentTokenIndex < tokens.size() && currentToken.identification.equals("STAR")) {
         Token starToken = currentToken;
         advance();

         ParseTreeNode f = parseF();
         ParseTreeNode tPrime = parseTPrime();

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("STAR", "*", null));
         children.add(f);
         children.add(tPrime);

         return new ParseTreeNode("T'", null, children);
      } else if (currentTokenIndex < tokens.size() && currentToken.identification.equals("SLASH")) {
         Token slashToken = currentToken;
         advance();

         ParseTreeNode f = parseF();
         ParseTreeNode tPrime = parseTPrime();

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("SLASH", "/", null));
         children.add(f);
         children.add(tPrime);

         return new ParseTreeNode("T'", null, children);
      }

      // ε production - empty node
      return new ParseTreeNode("T'", "ε", null);
   }

   // F → * F | & F | ( E ) | id
   private ParseTreeNode parseF() {
      if (currentTokenIndex < tokens.size() && currentToken.identification.equals("STAR")) {
         advance();
         ParseTreeNode f = parseF();

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("STAR", "*", null));
         children.add(f);

         return new ParseTreeNode("F", null, children);
      } else if (currentTokenIndex < tokens.size() && currentToken.identification.equals("AMP")) {
         advance();
         ParseTreeNode f = parseF();

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("AMP", "&", null));
         children.add(f);

         return new ParseTreeNode("F", null, children);
      } else if (currentTokenIndex < tokens.size() && currentToken.identification.equals("LPAREN")) {
         advance();
         ParseTreeNode e = parseE();

         if (!match("RPAREN")) {
            throw new RuntimeException("Expected closing parenthesis");
         }

         List<ParseTreeNode> children = new java.util.ArrayList<>();
         children.add(new ParseTreeNode("LPAREN", "(", null));
         children.add(e);
         children.add(new ParseTreeNode("RPAREN", ")", null));

         return new ParseTreeNode("F", null, children);
      } else if (currentTokenIndex < tokens.size() && currentToken.identification.equals("ID")) {
         String idValue = currentToken.value;
         advance();

         return new ParseTreeNode("F", null,
               java.util.Arrays.asList(new ParseTreeNode("ID", idValue, null)));
      } else {
         throw new RuntimeException("Unexpected token in F: " +
               (currentTokenIndex < tokens.size() ? currentToken.identification : "EOF"));
      }
   }

   /**
    * Parses tokens and returns the parse tree
    */
   public ParseTreeNode parse(List<Token> tokens) {
      this.tokens = tokens;
      this.currentTokenIndex = 0;

      if (tokens.isEmpty()) {
         throw new RuntimeException("Empty token list");
      }

      currentToken = tokens.get(currentTokenIndex);
      ParseTreeNode root = parseE();

      if (currentTokenIndex < tokens.size()) {
         throw new RuntimeException("Unexpected tokens at end of input");
      }

      return root;
   }
}