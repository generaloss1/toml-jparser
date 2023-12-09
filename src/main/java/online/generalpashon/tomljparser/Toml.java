package online.generalpashon.tomljparser;

import online.generalpashon.tomljparser.parser.Token;
import online.generalpashon.tomljparser.parser.TokenType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.UnexpectedException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Toml{

    public static class Value{

        public enum Quotes{
            NONE     (""),
            LITERAL_1("'"),
            LITERAL_2("\""),
            AUTO     ("");

            private final String quote;

            Quotes(String quote){
                this.quote = quote;
            }

            public String getQuote(){
                return quote;
            }

            public static Quotes fromString(String string){
                return switch(string){
                    default -> NONE;
                    case "'" -> LITERAL_1;
                    case "\"" -> LITERAL_2;
                };
            }
        }

        public Object value;
        public Quotes quotes;

        public Value(Token token){
            this.value = token.string;
            this.quotes = token.type.isLiteral() ?
                ((token.type == TokenType.LITERAL_1) ? Quotes.LITERAL_1 :
                    (token.type == TokenType.LITERAL_2) ? Quotes.LITERAL_2 :
                        Quotes.NONE) :
                Quotes.NONE;
        }

        public Value(Object object){
            this.value = object;
            if(object instanceof String)
                this.quotes = Quotes.AUTO;
        }

    }

    private final String name;
    private final Map<String, Value> map;

    public Toml(String name){
        this.map = new LinkedHashMap<>();
        this.name = name;
    }

    public Toml(){
        this("root");
    }

    public String getName(){
        return name;
    }

    public Map<String, Value> getMap(){
        return map;
    }


    public Object get(String key){
        final Value value = getValue(key);
        if(value == null)
            return null;
        return value.value;
    }

    private Value getValue(String key){
        if(key.contains(".")){
            final String[] chain = key.split("\\.");
            final int lastIdx = chain.length - 1;

            Toml target = this;
            for(int i = 0; i < lastIdx; i++){
                final String link = chain[i];
                target = target.getToml(link);
                if(target == null)
                    return null;
            }

            final String lastLink = chain[lastIdx];
            return target.map.get(lastLink);
        }

        return map.get(key);
    }

    private Toml setValue(String key, Value value){
        if(key.contains(".")){
            final String[] chain = key.split("\\.");
            final int lastIdx = chain.length - 1;

            Toml target = this;
            for(int i = 0; i < lastIdx; i++){
                final String link = chain[i];

                Toml next = target.getToml(link);
                if(next == null){
                    next = new Toml(link);
                    target.set(link, next);
                }
                target = next;
            }

            final String lastLink = chain[lastIdx];
            target.map.put(lastLink, value);
            return this;
        }

        map.put(key, value);
        return this;
    }

    public Toml getToml(String key){
        final Value value = getValue(key);
        if(value == null)
            return null;

        return (Toml) value.value;
    }

    public String getString(String key){
        return getValue(key).value.toString();
    }

    public Byte getByte(String key){
        return Byte.parseByte(getString(key));
    }

    public short getShort(String key){
        return Short.parseShort(getString(key));
    }

    public int getInt(String key){
        return Integer.parseInt(getString(key));
    }

    public long getLong(String key){
        return Long.parseLong(getString(key));
    }

    public float getFloat(String key){
        return Float.parseFloat(getString(key));
    }

    public double getDouble(String key){
        return Double.parseDouble(getString(key));
    }

    public boolean getBool(String key){
        return Boolean.parseBoolean(getString(key));
    }

    public char getChar(String key){
        return getString(key).charAt(0);
    }


    public Toml set(String key, Object value){
        return setValue(key, new Value(value));
    }

    public boolean has(String key){
        return map.containsKey(key);
    }

    public Toml remove(String key){
        map.remove(key);
        return this;
    }

    @Override
    public String toString(){
        return getName();
    }


    public Toml read(InputStream inputStream) throws NullPointerException, IOException{
        return new TomlReader(inputStream, this).read();
    }

    public Toml read(File file) throws NullPointerException, IOException{
        return read(new FileInputStream(file));
    }

    public Toml readFile(String filepath) throws NullPointerException, IOException{
        return read(new FileInputStream(filepath));
    }

    public Toml readResource(String name, Class<?> classLoader) throws NullPointerException, IOException{
        return read(classLoader.getResourceAsStream(name));
    }

}
