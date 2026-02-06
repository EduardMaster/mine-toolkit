plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "br.com.eduard"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "br.com.eduard"
            artifactId = "javautils"
            version = project.version as String
            from(components["java"])
        }
    }
}


repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("com.google.code.gson:gson:2.11.0")
   // compileOnly("org.bukkit:spigot:1.8.9")
}
