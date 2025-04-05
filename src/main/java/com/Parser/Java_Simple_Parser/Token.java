package com.Parser.Java_Simple_Parser;

public class Token{
    public String value;
    public String identification;
    private Token leftToken;
    private Token rightToken;

    public Token(String identification, String value){
        this.identification = identification;
        this.value = value;
    }
}
