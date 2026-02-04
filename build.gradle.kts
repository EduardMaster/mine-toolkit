plugins {
    java
    kotlin("jvm") version "2.1.10"//"2.3.0"//
    `maven-publish`
   // id("com.github.johnrengelman.shadow") version "6.1.0"
}
group = "br.com.eduard"
version = "2.0"

java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25


repositories {
    mavenCentral()
    mavenLocal()
    maven("https://libraries.minecraft.net/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
    compileOnly(kotlin("stdlib"))
    api(project(":JavaUtils"))
    api(project(":Storage"))
    api(project(":MineUtils"))
    api(project(":SQLManager"))
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")



    testCompileOnly("junit", "junit", "4.12")
    //testCompile("org.bukkit:spigot:1.8.9")

}


publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
