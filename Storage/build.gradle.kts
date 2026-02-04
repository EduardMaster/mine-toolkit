plugins {
    java
    kotlin("jvm")
    `maven-publish`
}

group = "br.com.eduard"
version = "1.0-SNAPSHOT"


java.sourceCompatibility = JavaVersion.VERSION_25
java.targetCompatibility = JavaVersion.VERSION_25

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "br.com.eduard"
            artifactId = "storage"
            version = project.version as String
            from(components["java"])
        }
    }
}


repositories {
    mavenCentral()
    mavenLocal()

}

dependencies {
    compileOnly(kotlin("stdlib"))
    compileOnly(project(":JavaUtils"))
    compileOnly("com.google.code.gson:gson:2.11.0")
}
