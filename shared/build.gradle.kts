plugins {
    id("org.jetbrains.kotlin.multiplatform")
    id("com.android.library")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

kotlin {
    androidTarget()
    listOf(iosArm64(), iosSimulatorArm64()).forEach { target ->
        target.binaries.framework {
            baseName = "A11yLensShared"
            isStatic = true
        }
    }
    jvm("desktop")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.ui)
            }
        }
        val desktopMain by getting {
            dependencies { implementation(compose.desktop.currentOs) }
        }
    }
}

android {
    namespace = "com.premsingh.a11ylens.shared"
    compileSdk = 35
    defaultConfig { minSdk = 24 }
}

compose.desktop {
    application { mainClass = "com.premsingh.a11ylens.kmp.MainKt" }
}
