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
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")

    implementation("org.jetbrains:annotations:26.0.2-1")
    compileOnly("org.projectlombok:lombok:1.18.40")
    annotationProcessor("org.projectlombok:lombok:1.18.40")
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
    }
}
