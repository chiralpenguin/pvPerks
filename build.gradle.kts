plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.purityvanilla"
version = "1.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("dev.folia", "folia-api", "1.21.6-R0.1-SNAPSHOT")
    compileOnly("com.purityvanilla", "pvLib", "1.0")
    compileOnly("com.purityvanilla", "pvCore", "1.0")
    compileOnly("net.luckperms", "api", "5.4")
}

tasks.shadowJar {
    dependsOn(tasks.build)
    archiveClassifier.set("") // This removes the default "-all" classifier
    archiveFileName.set("pvPerks.jar")
}

val testServerPluginsPath: String by project
tasks {
    val copyToServer by registering(Copy::class, fun Copy.() {
            dependsOn("shadowJar")
            from(layout.buildDirectory.file("libs"))
            include("pvPerks.jar") // Change to "plugin-version.jar" if no shadowing
            into(file(testServerPluginsPath)) // Use the externalized path here
        })
}
