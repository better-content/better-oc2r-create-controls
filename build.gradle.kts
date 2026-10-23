import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    eclipse
    `maven-publish`
    jacoco
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("net.minecraftforge.gradle") version "6.0.54"
}

val minecraftVersion = property("minecraft_version") as String
val forgeVersion = property("forge_version") as String
val kotlinForForgeVersion = property("kotlinforforge_version") as String
val createReleaseVersion = property("create_release_version") as String
val createMavenVersion = property("create_maven_version") as String
val ponderVersion = property("ponder_version") as String
val flywheelVersion = property("flywheel_version") as String
val registrateVersion = property("registrate_version") as String
val oc2rVersion = property("oc2r_version") as String
val architecturyVersion = property("architectury_version") as String
val markdownManualProjectId = property("markdown_manual_project_id") as String
val markdownManualFileId = property("markdown_manual_file_id") as String
val modId = property("mod_id") as String
val modName = property("mod_name") as String
val modVersion = property("mod_version") as String
val modAuthors = property("mod_authors") as String
val modDescription = property("mod_description") as String
val modLicense = property("mod_license") as String
val modIssueTrackerUrl = property("mod_issue_tracker_url") as String

group = property("mod_group") as String
version = modVersion

base {
    archivesName.set("better-oc2r-create-controls")
}

fun deobf(notation: String): Any =
    requireNotNull(extensions.getByName("fg").withGroovyBuilder { "deobf"(notation) })

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
    withSourcesJar()
}

kotlin {
    jvmToolchain(17)
}

minecraft {
    mappings("official", minecraftVersion)
    copyIdeResources = true

    runs {
        configureEach {
            workingDirectory(project.file("run"))
            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "info")
            property("forge.enabledGameTestNamespaces", modId)
            property("mixin.env.remapRefMap", "true")
            property("mixin.env.refMapRemappingFile", "${projectDir}/build/createSrgToMcp/output.srg")

            mods {
                create(modId) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client")

        create("server") {
            arg("--nogui")
        }

        create("gameTestServer")
    }
}

repositories {
    mavenCentral()
    maven("https://maven.minecraftforge.net")
    maven("https://cursemaven.com")
    maven("https://maven.architectury.dev/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://maven.createmod.net")
    maven("https://maven.ithundxr.dev/mirror")
    maven("https://api.modrinth.com/maven") {
        content {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:$minecraftVersion-$forgeVersion")

    implementation("thedarkcolour:kotlinforforge:$kotlinForForgeVersion")
    implementation(deobf("com.simibubi.create:create-$minecraftVersion:$createMavenVersion:slim"))
    implementation(deobf("net.createmod.ponder:Ponder-Forge-$minecraftVersion:$ponderVersion"))
    implementation("io.github.llamalad7:mixinextras-forge:0.5.4")
    compileOnly(deobf("dev.engine-room.flywheel:flywheel-forge-api-$minecraftVersion:$flywheelVersion"))
    runtimeOnly(deobf("dev.engine-room.flywheel:flywheel-forge-$minecraftVersion:$flywheelVersion"))
    implementation(deobf("com.tterrag.registrate:Registrate:$registrateVersion"))

    compileOnly(deobf("maven.modrinth:F1gm4RsH:$oc2rVersion"))
    runtimeOnly(deobf("maven.modrinth:F1gm4RsH:$oc2rVersion"))
    runtimeOnly(deobf("dev.architectury:architectury-forge:$architecturyVersion"))
    runtimeOnly(deobf("curse.maven:markdownmanual-$markdownManualProjectId:$markdownManualFileId"))

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
}

tasks.processResources {
    val props = mapOf(
        "minecraftVersion" to minecraftVersion,
        "forgeVersion" to forgeVersion,
        "kotlinForForgeVersion" to kotlinForForgeVersion,
        "createReleaseVersion" to createReleaseVersion,
        "oc2rVersion" to oc2rVersion,
        "modId" to modId,
        "modName" to modName,
        "modVersion" to modVersion,
        "modAuthors" to modAuthors,
        "modDescription" to modDescription,
        "modIssueTrackerUrl" to modIssueTrackerUrl,
        "modLicense" to modLicense
    )

    inputs.properties(props)
    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(props)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(17)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "17"
}

tasks.named<Jar>("jar") {
    finalizedBy("reobfJar")
}

val stageRuntimeJar by tasks.registering(Copy::class) {
    group = "build"
    description = "Stages the reobfuscated runtime jar into build/libs using the canonical release filename."
    dependsOn(tasks.named("reobfJar"))
    from(layout.buildDirectory.file("reobfJar/output.jar"))
    into(layout.buildDirectory.dir("libs"))
    rename { "${base.archivesName.get()}-$version.jar" }
}

tasks.named("assemble") {
    dependsOn(stageRuntimeJar)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.register("headlessGameTest") {
    group = "verification"
    description = "Runs Forge game tests in a headless dedicated server."
    dependsOn(tasks.named("runGameTestServer"))
}

tasks.register("verifyFast") {
    group = "verification"
    description = "Runs deterministic unit/resource checks without Forge game tests."
    dependsOn(tasks.named("check"))
}

tasks.register("verifyFull") {
    group = "verification"
    description = "Runs the full verification lane including headless Forge game tests."
    dependsOn(tasks.named("verifyFast"))
    dependsOn(tasks.named("headlessGameTest"))
}

// Source native tests use a fresh UUID RunConfig, never Delete(run/world).
apply(from = "gradle/native-gametest-fixture.gradle")

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test)
    violationRules {
        rule {
            element = "CLASS"
            includes = listOf(
                "com.bettercontent.betteroc2rcreatecontrols.compat.events.SpeedometerEventTracker",
                "com.bettercontent.betteroc2rcreatecontrols.compat.events.StressometerEventTracker"
            )
            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "1.00".toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}

// Native structures are copied to the authoritative RunConfig by the fixture script.
