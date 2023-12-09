# Java TOML lib (DON'T USE IT: Can’t parse arrays)
### Features:
* Simple
* High-customizable writer

## Get started:
```java
// Create toml
Toml toml = new Toml("name (default = root)");
// Read toml
Toml toml = new Toml().readFile("to_read.toml"); // filepath / stream / file / resource

// Get values
toml.getString("title")
toml.getToml("owner")
toml.getBool("database.enabled")
toml.get("") // returns object that can be stringified

// Set / put values
toml.set("title", "TOML Example");
toml.set("servers.beta.ip", "10.0.0.2");

// Write toml
TomlWriter writer = new TomlWriter()
    .indent(4) // or "   "
    .flat(false)
    .startsWithWrap(false)
    .wrapSubTables(false)
    .spacesAroundAssign(1) // or " "
    .defaultQuotes("'") // "'" or "\""
    .forceQuotes("") //: "" or "\"" or "'"
    .wrapsAfterTables("\n"); //: "\n" or "\n\n" or 0 or 1 or other
    
writer.writeFile(toml, "to_write.toml"); // filepath / stream / file / resource
```

## The following may be parsed:
```toml
string1 = 'text'
string2 = "text"

integer = 12345
float1 = -1.2e-9
float2 = -54E-2

[table]
    [subtable]
        key = 'value'

[display]
    depth = 24
[display.size]
    width = 1280
    height = 720
    
[server.beta.data]
    has-BD = true
    
```