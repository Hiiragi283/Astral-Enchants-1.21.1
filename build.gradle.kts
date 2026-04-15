plugins {
    `java-library`
    id("net.neoforged.moddev") version "2.0.116"
}

val parchment_minecraft_version: String by project
val parchment_mappings_version: String by project
val mod_version: String by project
val mod_group_id: String by project
val mod_id: String by project
val minecraft_version: String by project
val minecraft_version_range: String by project
val neo_version: String by project
val loader_version_range: String by project
val mod_name: String by project
val mod_license: String by project
val mod_authors: String by project
val mod_description: String by project

version = mod_version
group = mod_group_id

tasks.named<Wrapper>("wrapper").configure {
    distributionType = Wrapper.DistributionType.BIN
}

repositories {
    mavenLocal()
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    // accessories
    maven { url = uri("https://maven.wispforest.io/releases") }
    maven { url = uri("https://maven.su5ed.dev/releases") }
    maven { url = uri("https://maven.fabricmc.net") }

    // apotheosis
    maven {
        name = "Shadows of Fire"
        url = uri("https://maven.shadowsoffire.dev/releases")
    }
    mavenCentral()
}

base {
    archivesName.set(mod_id)
}

java {
    withSourcesJar()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

// 1. datagen ソースセットの定義
sourceSets {
    val main = getByName("main")
    create("datagen") {
        java.srcDir("src/datagen/java")
        // ここは「手動で作成するリソース（既存のファイルなど）」を置く場所
        resources.srcDir("src/datagen/resources")

        compileClasspath += main.compileClasspath + main.output
        runtimeClasspath += main.runtimeClasspath + main.output
    }
}

// 2. main ソースセットの設定
// 「datagenが自動生成した場所」だけを main に取り込む
sourceSets.main.get().resources {
    srcDir("src/datagen/generated/resources")
}

val localRuntime by configurations.creating

configurations {
    // datagen の構成が既存の implementation (NeoForge等を含む) を継承するように強制
    named("datagenImplementation") {
        extendsFrom(implementation.get())
    }
    named("datagenRuntimeOnly") {
        extendsFrom(runtimeOnly.get())
    }
    named("runtimeClasspath") {
        extendsFrom(localRuntime)
    }
}

neoForge {
    version = neo_version

    parchment {
        mappingsVersion = parchment_mappings_version
        minecraftVersion = parchment_minecraft_version
    }

    runs {
        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }

        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            gameDirectory = file("run-server")
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            data()
            gameDirectory = file("run-data")
            sourceSet = sourceSets.getByName("datagen")
            programArguments.addAll("--mod", mod_id, "--all",
                "--output", file("src/datagen/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath,
                "--existing", file("src/datagen/resources").absolutePath
            )
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
            sourceSet(sourceSets.getByName("datagen"))
        }
    }
}

dependencies {
    "datagenCompileOnly"("org.jetbrains:annotations:26.0.2")

    compileOnly("io.wispforest:accessories-neoforge:${project.findProperty("accessories_version")}")

    val placebo_version: String by project
    val apoth_attributes_version: String by project
    val apoth_spawners_version: String by project
    val apoth_ench_version: String by project
    val apoth_version: String by project

    compileOnly("dev.shadowsoffire:Placebo:$minecraft_version-$placebo_version")
    compileOnly("dev.shadowsoffire:ApothicAttributes:$minecraft_version-$apoth_attributes_version")
    compileOnly("dev.shadowsoffire:ApothicSpawners:$minecraft_version-$apoth_spawners_version")
    compileOnly("dev.shadowsoffire:ApothicEnchanting:$minecraft_version-$apoth_ench_version")
    compileOnly("dev.shadowsoffire:Apotheosis:$minecraft_version-$apoth_version")

    val l2hostility_version: String by project
    val l2complements_version: String by project
    val l2library_version: String by project
    val patchouli_version: String by project
    val curios_version: String by project

    compileOnly("maven.modrinth:l2hostility:$l2hostility_version")
    compileOnly("maven.modrinth:l2-complements:$l2complements_version")
    compileOnly("maven.modrinth:l2library:$l2library_version")

    "localRuntime"("maven.modrinth:l2hostility:$l2hostility_version")
    "localRuntime"("maven.modrinth:l2-complements:$l2complements_version")
    "localRuntime"("maven.modrinth:l2library:$l2library_version")
    "localRuntime"("maven.modrinth:patchouli:$patchouli_version")
    "localRuntime"("maven.modrinth:curios:$curios_version")
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version" to neo_version,
        "loader_version_range" to loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description,
        "accessories_version_range" to project.findProperty("accessories_version_range"),
        "l2hostility_version_range" to project.findProperty("l2hostility_version_range")
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources {
    srcDir(generateModMetadata.map { it.outputs })
}
neoForge.ideSyncTask(generateModMetadata)