package online.generalpashon.tomljparser.parser;

import java.util.*;
import java.util.regex.Matcher;

public class TomlLexer{

    public static Queue<Token> tokenize(String input){
        final Deque<Token> tokens = new ArrayDeque<>();

        int index = 0;
        cycle:
        while(index < input.length()){
            final String substring = input.substring(index);

            for(TokenType type: TokenType.values()){
                final Matcher matcher = type.matcher(substring);
                if(matcher.find()){
                    tokens.addLast(new Token(type, matcher.group()));
                    index += matcher.end();
                    continue cycle;
                }
            }

            index++;
        }

        return tokens;
    }

}
