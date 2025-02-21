plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "im.ghosty"
version = "1.4.2"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    //maven("https://haoshoku.xyz:8081/repository/default/")
    //maven("https://mvn.intelligence-modding.de/haoshoku")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":API"))
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.17.0")
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.4")

    implementation("com.github.Ghosty920.NickAPI:Main:75fe1ec307")
    implementation("com.samjakob:SpiGUI:v1.3.1")
    implementation("net.wesjd:anvilgui:1.10.4-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
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
