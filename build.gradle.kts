buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.google.gms:google-services:4.3.15")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath ("com.android.tools.build:gradle:8.9.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")// Ensure this is included


        

    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.benmanes.versions)
    id("com.google.dagger.hilt.android") version "2.50" apply false
}


