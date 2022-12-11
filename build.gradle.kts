import org.gradle.api.JavaVersion.VERSION_17
import org.gradle.api.file.DuplicatesStrategy.*
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    `java-library`
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

group = "u.scripts"
version = System.getenv("VERSION") ?: "DEV-SNAPSHOT"
description = "An application property generator framework that validates your property values on runtime."

repositories(RepositoryHandler::mavenCentral)

dependencies {
    implementation(libs.kotlin.stdlib)

    testImplementation(rootProject.libs.mockk)
    testImplementation(rootProject.libs.junit.jupiter)
    testImplementation(rootProject.libs.kotest.assertions.core)
    testImplementation(rootProject.libs.kotest.runner.junit5)
}

tasks {
    test {
        useJUnitPlatform()
        testLogging { showStandardStreams = true }
    }

    jar { enabled = false }

    register<Jar>("notes") {
        group = "skripts"
        description = "Generates a JAR file containing required classes to run \"u.skripts.notes.main.kt\""
        duplicatesStrategy = EXCLUDE
        manifest.apply {
            this.withDefaultManifestDetails()
            attributes["Main-Class"] = "u.skripts.notes.Launcher"
        }
        archiveFileName.set("u-notes.jar")
        from(sourceSets.main.get().output)
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get()
                .filter { it.name.endsWith("jar") }
                .map { zipTree(it) }
        })
        include("kotlin/**", "u/skripts/notes/**", "u/skripts/helpers/**")
    }

    java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }
    withType<KotlinCompile>().configureEach { kotlinOptions.jvmTarget = VERSION_17.toString() }

    wrapper { distributionType = DistributionType.ALL }
}


fun Manifest.withDefaultManifestDetails() {
    attributes["Implementation-Title"] = project.name
    attributes["Implementation-Version"] = project.version
    attributes["Implementation-Vendor"] = "u-ways"
}
