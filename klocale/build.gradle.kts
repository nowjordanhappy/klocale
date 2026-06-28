import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
}

group = "io.github.nowjordanhappy"
version = "1.0.1"

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
        // iosTest is auto-created by Kotlin 2.x default hierarchy; no manual setup needed.
        androidInstrumentedTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.androidx.test.ext.junit)
            implementation(libs.androidx.test.runner)
            implementation(libs.androidx.test.core)
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("io.github.nowjordanhappy", "klocale", "1.0.1")

    pom {
        name = "KLocale"
        description = "Kotlin Multiplatform library for in-app language switching on Android and iOS. No Compose dependency — exposes a StateFlow<String>."
        url = "https://github.com/nowjordanhappy/klocale"
        licenses {
            license {
                name = "MIT License"
                url = "https://opensource.org/licenses/MIT"
            }
        }
        developers {
            developer {
                id = "nowjordanhappy"
                name = "Jordan Rojas"
                email = "nowjordanhappy@gmail.com"
            }
        }
        scm {
            url = "https://github.com/nowjordanhappy/klocale"
            connection = "scm:git:git://github.com/nowjordanhappy/klocale.git"
            developerConnection = "scm:git:ssh://github.com/nowjordanhappy/klocale.git"
        }
    }
}
