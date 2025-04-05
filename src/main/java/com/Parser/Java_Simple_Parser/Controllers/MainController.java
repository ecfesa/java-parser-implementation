package com.Parser.Java_Simple_Parser.Controllers;

import java.util.ArrayList;
import java.util.List;

import com.Parser.Java_Simple_Parser.Token;
import com.Parser.Java_Simple_Parser.Tokenizer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {

        String input = "Primeiro * Segundo + Terceiro * (Quarto)";
        input = input.replaceAll(" ", "");

        List<Token> tokens = Tokenizer.tokenizer(input);

        System.out.println("Tokenização funcional!");
        for(Token token : tokens){
            System.out.println(token.identification + ":" +  token.value);
        }

        return "index";
    }

}