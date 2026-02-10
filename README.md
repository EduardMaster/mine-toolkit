# MineToolkit 2.0
## A Toolkit for Developing Bukkit/BungeeCord/Spigot/Paper Plugins

### Features
- Custom Database ORM based on **Hibernate** called "SQLMaanger"
- Custom YAML Implememntation (Simple) => (Config, ConfigSection)
- Custom Object Mapper (Transforming Objects in HashMap) called "Storage"
- Auto loading of Java Libraries in folder /server/libs/ works until Java 21

### Ideias
- Auto downloading of Kotlin Libraries from Maven repository to make usage of this Toolkit easy

### Helpers
- Usage of Database with class **AutoSQL**
- Creating Inventories with Actions (Menus) with classes **Menu** , **MenuButton** e **Shop**
- Creating Scoreboard (Informative Screens) more easy with class **DisplayBoard**
- Helpers classes for Java data control
- Kotlin Extensions for many functions of Toolkit

### OldNames
- EduardLib 1.0
- EduardAPI 1.7

### How to Install

#### Gradle

```kts
repositories {
   maven("https://jitpack.io")
}

dependencies {
    api("com.github.EduardMaster:mine-toolkit:main-SNAPSHOT") // last-version
    api("com.github.EduardMaster:mine-toolkit:1.0.0") // v1.0-version
}
```
#### Maven

```xml
<repositories>
    <repository>
        <id>Jitpack</id>
        <url>https://jitpack.com/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.EduardMaster</groupId>
        <artifactId>mine-toolkit</artifactId>
        <version>1.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```



