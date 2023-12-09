package online.generalpashon.tomljparser;

public class Test{

    public static void main(String[] args) throws Exception{
        Toml toml = new Toml().readFile("read.toml");

        toml.set("title", "TOML Example");
        toml.set("servers.beta.ip", "10.0.0.2");

        System.out.println(toml.get("servers.beta.ip"));
        System.out.println(toml.get("table.subtable.key"));

        TomlWriter writer = new TomlWriter()
            .indent(4) // or "   "
            .flat(false)
            .startsWithWrap(false)
            .wrapSubTables(false)
            .spacesAroundAssign(1) // or " "
            .defaultQuotes("'") // "'" or "\""
            .forceQuotes("") //: "" or "\"" or "'"
            .wrapsAfterTables("\n"); //: "\n" or "\n\n" or 0 or 1 or other

        writer.writeFile(toml, "write.toml");
    }

}
