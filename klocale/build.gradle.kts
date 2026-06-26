plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    id("maven-publish")
}

group = "com.nowjordanhappy"
version = "1.0.0-LOCAL"

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    androidTarget {
        publishLibraryVariants("release")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        androidMain.dependencies {
            implementation(libs.androidx.appcompat)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        val iosTest by creating {
            dependsOn(commonTest.get())
        }
        getByName("iosX64Test").dependsOn(iosTest)
        getByName("iosArm64Test").dependsOn(iosTest)
        getByName("iosSimulatorArm64Test").dependsOn(iosTest)
        androidInstrumentedTest.dependencies {
            implementation(libs.androidx.test.ext.junit)
        }
    }
}

android {
    namespace = "com.nowjordanhappy.klocale"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}
