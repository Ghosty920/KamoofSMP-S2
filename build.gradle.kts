plugins {
    id("java")
    id("com.gradleup.shadow") version ("8.3.0")
}

group = "me.ghosty"
version = "2.0.0"

repositories {
    mavenCentral()
    maven("https://mvn.intelligence-modding.de/haoshoku")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://haoshoku.xyz:8081/repository/default/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
//    compileOnly("xyz.haoshoku.nick:nickapi:7.0-SNAPSHOT")
    compileOnly("xyz/haoshoku/nick:NickAPI:v6.7")

    implementation("net.kyori:adventure-api:4.17.0")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.4")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    assemble {
        dependsOn(shadowJar)
    }

    processResources {
        inputs.property("version", version)

        filesMatching(listOf("plugin.yml")) {
            expand(inputs.properties)
        }
    }
}
