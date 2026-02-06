plugins {
    java
    kotlin("jvm") version "2.3.0"//"2.1.10"//
    `maven-publish`

   // id("com.github.johnrengelman.shadow") version "6.1.0"
}
group = "br.com.eduard"
version = "2.0"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21


java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
/*
tasks.withType<JavaCompile> {
    // Usamos target/source em vez de .release para evitar o erro de módulos
    sourceCompatibility = "23"
    targetCompatibility = "23"
    options.encoding = "UTF-8"
}
*/

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
    }
}

/*
tasks.withType<JavaCompile> {
    options.release.set(21)
    options.encoding = "UTF-8"
}
*/
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
    compileOnly( "org.projectlombok:lombok:1.18.34")
    annotationProcessor ("org.projectlombok:lombok:1.18.34")
    compileOnly(kotlin("stdlib"))
    api(project(":JavaUtils"))
    api(project(":Storage"))
    api(project(":MineUtils"))
    api(project(":SQLManager"))
    compileOnly("org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.21-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.mojang:authlib:6.0.54")


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
