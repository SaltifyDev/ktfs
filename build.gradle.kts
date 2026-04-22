@file:OptIn(ExperimentalKotlinGradlePluginApi::class, ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform") version "2.3.20"
    id("com.vanniktech.maven.publish") version "0.36.0"
}

group = "org.ntqqrev"
version = "0.1.1"

repositories {
    mavenCentral()
}

kotlin {
    jvm()

    js {
        browser()
        nodejs()
    }
    wasmJs {
        nodejs()
    }
    wasmWasi {
        nodejs()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    // tvosX64() - Deprecated since 2.3.20
    tvosArm64()
    tvosSimulatorArm64()

    watchosArm32()
    watchosArm64()
    // watchosX64() - Deprecated since 2.3.20
    watchosSimulatorArm64()
    watchosDeviceArm64()

    androidNativeArm32()
    androidNativeArm64()
    androidNativeX64()
    androidNativeX86()

    linuxX64()
    linuxArm64()
    // linuxArm32Hfp() - Deprecated since 2.3.20

    // macosX64() - Deprecated since 2.3.20
    macosArm64()

    mingwX64()

    applyDefaultHierarchyTemplate {
        common {
            group("wrapped") {
                withJvm()
                withJs()
                withWasmJs()
                withWasmWasi()
                withApple()
                withAndroidNative()
                withLinux()
            }
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-io-core:0.9.0")
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    jvmToolchain(21)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = project.group.toString(),
        artifactId = project.name,
        version = project.version.toString()
    )

    pom {
        name = project.name
        description = "A minimal wrapper for kotlinx.io.files with proper Windows non-ASCII path support"
        url = "https://github.com/SaltifyDev/ktfs"
        inceptionYear = "2026"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "Wesley-Young"
                name = "Wesley F. Young"
                email = "wesley.f.young@outlook.com"
            }
        }
        scm {
            connection = "scm:git:git://github.com/SaltifyDev/ktfs.git"
            developerConnection = "scm:git:ssh://github.com/SaltifyDev/ktfs.git"
            url = "https://github.com/SaltifyDev/ktfs"
        }
    }
}