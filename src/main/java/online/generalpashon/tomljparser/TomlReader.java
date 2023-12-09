package online.generalpashon.tomljparser;

import online.generalpashon.tomljparser.parser.Token;
import online.generalpashon.tomljparser.parser.TomlLexer;
import online.generalpashon.tomljparser.parser.TomlParser;

import java.io.*;
import java.rmi.UnexpectedException;
import java.util.Queue;

public class TomlReader{

    private final Toml toml;
    private final String input;

    public TomlReader(InputStream inputStream, Toml toml) throws NullPointerException, IOException{
        if(inputStream == null)
            throw new NullPointerException("Reading toml error: InputStream is null");
        if(toml == null)
            throw new NullPointerException("Reading toml error: Toml object is null");

        this.toml = toml;
        final DataInputStream dataInStream = new DataInputStream(inputStream);
        this.input = new String(dataInStream.readAllBytes());
    }

    public Toml read() throws UnexpectedException{
        final Queue<Token> tokens = TomlLexer.tokenize(input);
        final TomlParser parser = new TomlParser(tokens, toml);
        parser.parse();
        return toml;
    }

}
