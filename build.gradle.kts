plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "9.3.2"
}

group = "im.ghosty.kamoof"
version = "1.5.8"

repositories {
    mavenCentral()
//    mavenLocal()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    maven("https://repo.codemc.io/repository/ghosty920/")
    maven("https://jitpack.io")
    maven("https://mvn.wesjd.net/")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(project(":API"))
//    compileOnly("org.spigotmc:spigot-api:1.21.11-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot-api:26.1-R0.1-SNAPSHOT")
//    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.26.1")
    implementation("net.kyori:adventure-text-minimessage:4.26.1")
    implementation("net.kyori:adventure-text-serializer-legacy:4.26.1")
    implementation("net.kyori:adventure-platform-bungeecord:4.4.1")

    implementation("im.ghosty.nickapi:Main:v1.1.0")
//    implementation("com.samjakob:SpiGUI:v1.4.1")
    implementation(mapOf("name" to "SpiGUI-v2-2.0.0", "ext" to "jar"))
    implementation("net.wesjd:anvilgui:1.10.12-SNAPSHOT")


    compileOnly("org.projectlombok:lombok:1.18.44")
    annotationProcessor("org.projectlombok:lombok:1.18.44")
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
            exclude(dependency("im.ghosty.nickapi:Main:.*"))
            exclude(dependency(".*:SpiGUI-v2-2.0.0:.*"))
            exclude(dependency("net.wesjd:anvilgui:.*"))
        }
        relocate("com.samjakob.spigui", "im.ghosty.kamoof.deps.spigui")
        relocate("net.wesjd.anvilgui", "im.ghosty.kamoof.deps.anvilgui")
        relocate("im.ghosty.nickapi", "im.ghosty.kamoof.deps.nickapi")
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

        repositories {
            maven("https://repo.codemc.io/repository/Ghosty920/") {
                val mavenUsername = System.getenv("GRADLE_PROJECT_MAVEN_USERNAME")
                val mavenPassword = System.getenv("GRADLE_PROJECT_MAVEN_PASSWORD")
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
}

