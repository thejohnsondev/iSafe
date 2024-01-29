plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.thejohnsondev.model"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

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

}