package online.generalpashon.tomljparser.parser;

public class Token{

    public final TokenType type;
    public final String string;

    public Token(TokenType type, String string){
        if(type.isLiteral() || type.isTable())
            string = string.substring(1, string.length() - 1);
        if(type.isArray())
            string = string.substring(2, string.length() - 2);

        this.string = string;
        this.type = type;
    }


    @Override
    public String toString(){
        return "[" + type + "]: '" + (type.isNextLine() ? "" : string) + "'";
    }

}
