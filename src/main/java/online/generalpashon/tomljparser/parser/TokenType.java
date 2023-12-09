package online.generalpashon.tomljparser.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum TokenType{

    NEXT_LINE  ("next line", "^\\n"),
    SPACES     ("spaces", "^\\s+"),
    ARRAY      ("array", "^\\[\\[\\w\\]\\]"),
    TABLE      ("table", "^\\[.+\\]"),
    ASSIGN     ("=", "^={1}"),
    OPEN_BRACE ("[", "^\\[{1}"),
    CLOSE_BRACE("]", "^\\]{1}"),
    LITERAL_1  ("'", "^'.*'"),
    LITERAL_2  ("\"", "^\".*\""),
    STRING     ("string", "^[a-zA-Z-_0-9.]+");


    private final String string;
    private final Pattern pattern;

    TokenType(String string, String regex){
        this.string = string;
        this.pattern = Pattern.compile(regex);
    }

    public Matcher matcher(String input){
        return pattern.matcher(input);
    }


    @Override
    public String toString(){
        return string;
    }


    public boolean isString(){
        return this == TokenType.STRING;
    }

    public boolean isSpaces(){
        return this == TokenType.SPACES;
    }

    public boolean isNextLine(){
        return this == TokenType.NEXT_LINE;
    }

    public boolean isTable(){
        return this == TokenType.TABLE;
    }

    public boolean isArray(){
        return this == TokenType.ARRAY;
    }

    public boolean isLiteral(){
        return this == TokenType.LITERAL_2 || this == TokenType.LITERAL_1;
    }

}
