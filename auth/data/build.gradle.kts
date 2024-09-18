import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
}

val testProperties = Properties().apply {
    file("$rootDir/test.properties").inputStream().use { load(it) }
}

val authSecretKey: String? = gradleLocalProperties(rootDir).getProperty("auth_secret_key") ?: testProperties.getProperty("auth_secret_key")
val authSecretIV: String? = gradleLocalProperties(rootDir).getProperty("auth_secret_iv") ?: testProperties.getProperty("auth_secret_iv")

android {
    namespace = "com.thejohnsondev.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
        buildConfigField("String", "AUTH_SECRET_KEY", authSecretKey ?: "test_key")
        buildConfigField("String", "AUTH_SECRET_IV", authSecretIV ?: "test_iv")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation(project(":core:model"))
    implementation(project(":core:datastore"))
    implementation(project(":core:common"))
    implementation(project(":core:network"))
    implementation(project(":core:database"))

    implementation(libs.androidx.ktx)
    implementation(platform(libs.org.jetbrains.kotlin.bom))
    implementation(libs.androidx.appcompat)

    // Firebase
    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.com.google.firebase.analyticsktx)
    implementation(libs.com.google.firebase.crashlyticsktx)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Hilt-Dagger
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.com.google.dagger.hilt.compiler)

    // Arrow
    implementation(libs.arrow.core)

}