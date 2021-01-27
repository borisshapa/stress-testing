plugins {
    id("java")
    kotlin("jvm")
    kotlin("plugin.serialization")
    id("application")
    id("distribution")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.ktor:ktor-server-netty:1.4.0")
    implementation("io.ktor:ktor-client-apache:1.4.0")
    implementation("io.ktor:ktor-jackson:1.4.0")
    implementation("io.ktor:ktor-html-builder:1.4.0")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("org.jetbrains:kotlin-css:1.0.0-pre.110-kotlin-1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
    implementation("org.jetbrains.exposed:exposed-core:0.22.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.22.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.22.1")
    implementation("com.h2database:h2:1.4.200")
    implementation("com.beust:klaxon:5.0.1")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks.withType<Copy>().named("processResources") {
    from(project(":client").tasks.named("browserDistribution"))
}

tasks.register<Copy>("devServerResources") {
    destinationDir = File(project.buildDir, "dev-resources")
    from(sourceSets.main.map { it.resources })
    filter { line -> line.replace("port = 8080", "port = 8081") }
}

tasks.register("prepareDevServer") {
    dependsOn("compileKotlin")
    dependsOn("devServerResources")
}

tasks.register<JavaExec>("devServer") {
    dependsOn("prepareDevServer")

    classpath = project.files(
        configurations.runtimeClasspath,
        File(project.buildDir, "classes/kotlin/main"),
        File(project.buildDir, "dev-resources")
    )
    main = "io.ktor.server.netty.EngineMain"
}

task("stage") {
    dependsOn("installDist")
}