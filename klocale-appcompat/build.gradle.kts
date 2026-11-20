import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
}

group = "io.github.nowjordanhappy"
version = "2.0.0"

kotlin { jvmToolchain(libs.versions.jvmTarget.get().toInt()) }

android {
    namespace = "com.nowjordanhappy.klocale.appcompat"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    api(project(":klocale"))
    implementation(libs.androidx.appcompat)
    androidTestImplementation(kotlin("test"))
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.core)
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("io.github.nowjordanhappy", "klocale-appcompat", "2.0.0")

    pom {
        name = "KLocale AppCompat"
        description = "KLocale for apps on AppCompatActivity: the language through AppCompatDelegate, persisted below Android 13."
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
