package online.generalpashon.tomljparser;

import java.io.*;
import java.util.Map;
import java.util.Set;

public class TomlWriter{

    private String indent;
    private boolean flat;
    private String wrapsAfterTables;
    private boolean wrapSubTables;
    private boolean startsWithWrap;
    private String defaultQuotes;
    private String forceQuotes;
    private String spacesAroundAssign;
    private boolean alwaysChopTables;

    public TomlWriter(){
        indent(4);
        flat(false);
        wrapsAfterTables("\n");
        wrapSubTables(false);
        startsWithWrap(false);
        defaultQuotes("'");
        forceQuotes("");
        spacesAroundAssign("  ");
    }


    public String getIndent(){
        return indent;
    }

    public TomlWriter indent(String indent){
        this.indent = indent;
        return this;
    }

    public TomlWriter indent(int indent){
        return indent(" ".repeat(indent));
    }


    public boolean isFlat(){
        return flat;
    }

    public TomlWriter flat(boolean flat){
        this.flat = flat;
        return this;
    }


    public String getWrapsAfterTables(){
        return wrapsAfterTables;
    }

    public TomlWriter wrapsAfterTables(String wrapsAfterTables){
        this.wrapsAfterTables = wrapsAfterTables;
        return this;
    }

    public TomlWriter wrapsAfterTables(int wrapsAfterTables){
        return wrapsAfterTables("\n".repeat(wrapsAfterTables));
    }


    public boolean isWrapSubTables(){
        return wrapSubTables;
    }

    public TomlWriter wrapSubTables(boolean wrapSubTables){
        this.wrapSubTables = wrapSubTables;
        return this;
    }


    public boolean isStartsWithWrap(){
        return startsWithWrap;
    }

    public TomlWriter startsWithWrap(boolean startsWithWrap){
        this.startsWithWrap = startsWithWrap;
        return this;
    }


    public String getDefaultQuotes(){
        return defaultQuotes;
    }

    public TomlWriter defaultQuotes(String defaultQuotes){
        this.defaultQuotes = defaultQuotes;
        return this;
    }


    public String getForceQuotes(){
        return forceQuotes;
    }

    public TomlWriter forceQuotes(String forceQuotes){
        this.forceQuotes = forceQuotes;
        return this;
    }


    public String getSpacesAroundAssign(){
        return spacesAroundAssign;
    }

    public TomlWriter spacesAroundAssign(String spacesAroundAssign){
        this.spacesAroundAssign = spacesAroundAssign;
        return this;
    }

    public TomlWriter spacesAroundAssign(int assignSpaces){
        return spacesAroundAssign(" ".repeat(assignSpaces));
    }


    public boolean isAlwaysChopTables(){
        return alwaysChopTables;
    }

    public TomlWriter alwaysChopTables(boolean alwaysChopTables){
        this.alwaysChopTables = alwaysChopTables;
        return this;
    }


    public TomlWriter writeFile(Toml toml, String string) throws FileNotFoundException{
        return write(toml, new FileOutputStream(string));
    }

    public TomlWriter write(Toml toml, File file) throws FileNotFoundException{
        return write(toml, new FileOutputStream(file));
    }

    boolean firstLine = true;
    int lastIndent;
    boolean lastPair;

    public TomlWriter write(Toml toml, OutputStream outputStream){
        final PrintWriter writer = new PrintWriter(outputStream);
        writeTableElements(writer, 0, toml);
        writer.close();
        return this;
    }

    private void writeTable(PrintWriter writer, int indents, boolean first, Toml toml){
        if(firstLine && startsWithWrap){
            firstLine = false;
            writer.println();
        }
        if((lastPair && (wrapSubTables || indents == 0) && lastIndent == indents) || ((indents < lastIndent && (wrapSubTables || indents == 0)) && !first))
            writer.print(wrapsAfterTables);
        lastIndent = indents;

        final int decreaseIndents = flat ? 1 : 0;
        if(indents - decreaseIndents > 0)
            writer.print(indent.repeat(indents - decreaseIndents));

        writer.println("[" + toml.getName() + "]");

        writeTableElements(writer, indents + 1, toml);

        lastPair = false;
    }

    private void writeTableElements(PrintWriter writer, int indents, Toml toml){
        boolean firstTable = true;
        final Set<Map.Entry<String, Toml.Value>> entries = toml.getMap().entrySet();
        for(Map.Entry<String, Toml.Value> e: entries){
            final Toml.Value value = e.getValue();
            if(value.value instanceof Toml tomlValue)
                writeTable(writer, indents, firstTable, tomlValue);
            else
                writeKeyValue(writer, indents, e.getKey(), value);

            firstTable = false;
        }
    }

    private void writeKeyValue(PrintWriter writer, int indents, String key, Toml.Value value){
        if(firstLine && startsWithWrap){
            firstLine = false;
            writer.println();
        }
        if(indents < lastIndent)
            writer.print(wrapsAfterTables);
        lastIndent = indents;

        final String quotes = switch(value.quotes){
            case LITERAL_1 -> forceQuotes.isEmpty() ? "'" : forceQuotes;
            case LITERAL_2 -> forceQuotes.isEmpty() ? "\"" : forceQuotes;
            case AUTO -> forceQuotes.isEmpty() ? defaultQuotes : forceQuotes;
            case NONE -> "";
        };

        final int decreaseIndents = flat ? 1 : 0;
        if(indents - decreaseIndents > 0)
            writer.print(indent.repeat(indents - decreaseIndents));

        writer.println(key + spacesAroundAssign + "=" + spacesAroundAssign + quotes + value.value + quotes);

        lastPair = true;
    }

}
