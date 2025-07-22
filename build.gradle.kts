plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "im.ghosty"
version = "1.5.2"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":API"))
    compileOnly("org.spigotmc:spigot-api:1.21.6-R0.1-SNAPSHOT")
    //compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.23.0")
    implementation("net.kyori:adventure-text-minimessage:4.23.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.23.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.4.0")

    implementation("com.github.Ghosty920.NickAPI:Main:38acdca3e0")
    implementation("com.samjakob:SpiGUI:v1.4.1")
    implementation("net.wesjd:anvilgui:1.10.8-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
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

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        minimize {
            exclude(project(":API"))
            exclude(dependency("com.github.Ghosty920.NickAPI:Main:.*"))
            exclude(dependency("net.wesjd:anvilgui:.*"))
        }
        relocate("com.samjakob.spigui", "im.ghosty.kamoof.deps.com.samjakob.spigui")
        relocate("net.wesjd.anvilgui", "im.ghosty.kamoof.deps.net.wesjd.anvilgui")
        relocate("xyz.haoshoku.nick", "im.ghosty.kamoof.deps.xyz.haoshoku.nick")
    }

    processResources {
        inputs.property("version", version)

        filesMatching(listOf("plugin.yml")) {
            expand(inputs.properties)
        }
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = project.group.toString()
                artifactId = project.name
                version = project.version.toString()
            }
        }
    }
}
