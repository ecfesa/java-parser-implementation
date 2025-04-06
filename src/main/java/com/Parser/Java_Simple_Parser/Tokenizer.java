package com.Parser.Java_Simple_Parser;

import com.Parser.Java_Simple_Parser.Token;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public String value;
    public String identification;
    private Token leftToken;
    private Token rightToken;

    public static List<Token> tokenizer(String input) {

        List<Token> tokens = new ArrayList<Token>();

        int i = 0;

        while (i < input.length()) {
            char c = input.charAt(i);

            // Handle single-character tokens
            switch (c) {
                case '+':
                    tokens.add(new Token("PLUS", "+"));
                    break;
                case '*':
                    tokens.add(new Token("STAR", "*"));
                    break;
                case '/':
                    tokens.add(new Token("SLASH", "/"));
                    break;
                case '&':
                    tokens.add(new Token("AMP", "&"));
                    break;
                case '(':
                    tokens.add(new Token("LPAREN", "("));
                    break;
                case ')':
                    tokens.add(new Token("RPAREN", ")"));
                    break;
                default:

                    Pattern pattern = Pattern.compile("[+*/&()]");
                    int j = 1;
                    StringBuilder nonvariable = new StringBuilder();
                    nonvariable.append(input.charAt(i));

                    while ((i + j) < input.length() && !pattern.matcher(String.valueOf(input.charAt(i + j))).find()) {
                        nonvariable.append(input.charAt(i + j));
                        j++;
                    }
                    tokens.add(new Token("ID", input.substring(i, i + j)));

                    // Move i to after the token
                    i = i + j - 1;

                    break;
            }
            i++;
        }

        return tokens;

    }

}
