// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services)
    }
}
plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
    id("com.android.library") version "8.1.4" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.51" apply false
    id("com.google.firebase.crashlytics") version "2.9.9" apply false
    kotlin("kapt") version "1.7.20"
}