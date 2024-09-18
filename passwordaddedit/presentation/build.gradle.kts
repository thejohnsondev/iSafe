plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
}

android {
    namespace = "com.thejohnsondev.addpassword"
    compileSdk = 34

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        compose = true
    }
}

dependencies {

    implementation(project(":passwordaddedit:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:ui"))
    implementation(project(":core:datastore"))

    implementation(libs.androidx.ktx)
    implementation(platform(libs.org.jetbrains.kotlin.bom))
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    // Firebase
    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.com.google.firebase.analyticsktx)
    implementation(libs.com.google.firebase.crashlyticsktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.icons.extended)

    // Accompanist
    implementation(libs.com.google.accompanist.systemuicontroller)

    // Hilt-Dagger
    implementation(libs.com.google.dagger.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.com.google.dagger.hilt.compiler)

    // Arrow
    implementation(libs.arrow.core)

}