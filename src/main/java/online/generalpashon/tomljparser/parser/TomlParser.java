package online.generalpashon.tomljparser.parser;

import online.generalpashon.tomljparser.Toml;

import java.rmi.UnexpectedException;
import java.util.Queue;

public class TomlParser{

    private final Toml toml;

    private final Queue<Token> tokens;

    private int line, symbol;

    private Indents indents;
    private String expectsIndent;

    public TomlParser(Queue<Token> tokens, Toml toml){
        this.tokens = tokens;
        this.toml = toml;
        this.indents = new Indents();
        this.expectsIndent = "";
    }

    public void parse() throws UnexpectedException{
        while(!tokens.isEmpty()){
            line++;
            symbol = 0;

            if(tokens.peek().type.isNextLine()){
                next();
                if(tokens.isEmpty())
                    return;
                continue;
            }

            parseLine();
            require("Expects new line", TokenType.NEXT_LINE);
        }
    }

    private void parseLine() throws UnexpectedException{
        final int spacesSkipped = skipSpaces();
        final Token token = next();

        if(token.type.isTable()){

            if(!expectsIndent.isEmpty())
                indents.add(expectsIndent, spacesSkipped);
            else
                indents.removeTo(spacesSkipped);
            expectsIndent = token.string;

            String newTomlName = token.string;
            if(newTomlName.contains(".")){
                final String[] chain = newTomlName.split("\\.");
                newTomlName = chain[chain.length - 1];
            }

            final Toml newToml = new Toml(newTomlName);
            final Toml currentToml = getTomlByIndents();
            if(currentToml == null)
                return;

            final Toml targetToml = getTomlByKey(currentToml, token.string);
            if(targetToml == null)
                return;

            targetToml.set(newToml.getName(), newToml);

        }else if(token.type.isString()){

            if(!expectsIndent.isEmpty()){
                indents.add(expectsIndent, spacesSkipped);
                expectsIndent = "";
            }

            skipSpaces();
            require("Expects '='", TokenType.ASSIGN);
            skipSpaces();
            final Token value = require("Expects value", TokenType.STRING, TokenType.LITERAL_1, TokenType.LITERAL_2);

            final Toml currentIndentsToml = getTomlByIndents();
            if(currentIndentsToml == null)
                return;

            currentIndentsToml.getMap().put(token.string, new Toml.Value(value));
        }
    }


    private Toml getTomlByIndents(){
        Toml target = toml;
        for(Indents.Indent indent: indents.toCollection()){
            target = target.getToml(indent.key);
            if(target == null)
                return null;
        }
        return target;
    }

    private Toml getTomlByKey(Toml toml, String key){
        Toml target = toml;
        if(key.contains(".")){
            final String[] chain = key.split("\\.");
            for(int i = 0; i < chain.length - 1; i++){
                final String link = chain[i];
                target = target.getToml(link);
                if(target == null)
                    return null;
            }
        }
        return target;
    }

    private Token require(String errorMessage, TokenType... types) throws UnexpectedException{
        final Token token = next();
        for(TokenType type: types)
            if(token.type == type)
                return token;

        throw new UnexpectedException(errorMessage + ", but found '" + token.string + "' (" + line + ":" + symbol + ")");
    }

    private int skipSpaces(){
        int spaces = 0;
        while(!tokens.isEmpty()){
            final Token token = tokens.peek();
            if(!token.type.isSpaces())
                break;

            spaces += next().string.length();
        }

        return spaces;
    }

    private Token next(){
        if(tokens.isEmpty())
            return null;

        final Token token = tokens.poll();
        symbol += token.string.length();
        return token;
    }

}
