plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
    `maven-publish`
}

group = "net.jandie1505"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven {
        name = "chaossquad"
        url = uri("https://maven.chaossquad.net/snapshots")
    }
    maven {
        url = uri("https://repo.extendedclip.com/releases/")
    }
    maven {
        name = "jitpack"
        url = uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly ("me.clip:placeholderapi:2.11.6")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    implementation("net.chaossquad:mclib:master-6601f107899d7deaa7c9ba02809f52af5d233072")
}

java {}

tasks {
    shadowJar {
        relocate("net.chaossquad.mclib", "net.jandie1505.updatedisplayname.dependencies.net.chaossquad.mclib")
    }
    build {
        dependsOn(shadowJar)
    }
}

// gradle publish{PUBLICATION_NAME}To{REPOSITORY_NAME}Repository
// in this case: publishMavenToChaosSquadRepository
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "chaossquad"
            url = uri(if (version.toString().endsWith("RELEASE")) {
                "https://maven.chaossquad.net/releases"
            } else {
                "https://maven.chaossquad.net/snapshots"
            })

            credentials {
                username = findProperty("chaossquad-repository.username") as String?
                password = findProperty("chaossquad-repository.password") as String?
            }
        }
    }
}