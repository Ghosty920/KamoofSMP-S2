plugins {
    id("java")
    id("maven-publish")
    id("com.gradleup.shadow") version "8.3.9"
}

group = rootProject.group
version = rootProject.version

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.10-R0.1-SNAPSHOT")

    implementation("org.jetbrains:annotations:26.0.2")
    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")
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

    java {
        withJavadocJar()
        withSourcesJar()
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        minimize()
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
