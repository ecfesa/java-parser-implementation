package com.Parser.Java_Simple_Parser.Controllers;

import java.util.List;

import com.Parser.Java_Simple_Parser.Token;
import com.Parser.Java_Simple_Parser.Tokenizer;
import com.Parser.Java_Simple_Parser.Parser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {

        String input = "a)sdas)";
        input = input.replaceAll(" ", "");

        List<Token> tokens = Tokenizer.tokenizer(input);
        Parser parser = new Parser(tokens);

        System.out.println("Tokens: ");
        for (Token token : tokens) {
            System.out.println(token.type + ":" + token.value);
        }

        try{
            parser.parse();
        }
        catch (RuntimeException e){
            System.out.print(e);
        }

        return "validator";
    }

}
