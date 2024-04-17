/*
 * Copyright 2024 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("android-base-convention")
    id("dev.icerock.mobile.multiplatform.android-manifest")
}

kotlin {
    jvm()
    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    js(IR) {
        browser()
        useCommonJs()
    }

    sourceSets {
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

        val watchosMain by creating
        val watchos64Main by creating
        val watchos32Main by creating
        val watchosX64Main by creating
        val watchosArm64Main by creating
        val watchosSimulatorArm64Main by creating
        watchos64Main.dependsOn(watchosMain)
        watchos32Main.dependsOn(watchosMain)
        watchosX64Main.dependsOn(watchos64Main)
        watchosArm64Main.dependsOn(watchos32Main)
        watchosSimulatorArm64Main.dependsOn(watchos64Main)

        val commonMain by getting
        val macosArm64Main by getting
        val macosX64Main by getting

        val macosMain by creating {
            dependsOn(commonMain)
            macosArm64Main.dependsOn(this)
            macosX64Main.dependsOn(this)
        }

        val commonTest by getting
        val macosArm64Test by getting
        val macosX64Test by getting

        val macosTest by creating {
            dependsOn(commonTest)
            macosArm64Test.dependsOn(this)
            macosX64Test.dependsOn(this)
        }
        
        val jsTest by getting {
            dependsOn(commonTest)
        }
    }

    jvmToolchain(11)
}

tasks.withType<AbstractTestTask> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(
            TestLogEvent.SKIPPED,
            TestLogEvent.PASSED,
            TestLogEvent.FAILED
        )
        showStandardStreams = true
    }
    outputs.upToDateWhen { false }
}
