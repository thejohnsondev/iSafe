
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.thejohnsondev.isafe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.thejohnsondev.isafe"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val hiltVersion = "2.44"
    val coroutinesVersion = "1.7.1"

    implementation(project(":vault:presentation"))
    implementation(project(":vault:domain"))
    implementation(project(":vault:data"))
    implementation(project(":passwordaddedit:presentation"))
    implementation(project(":passwordaddedit:domain"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))

    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.2.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Splash screen api
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Accompanist utils
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.27.0")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.30.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion")

    // Hilt-Dagger
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Navigation
    implementation("androidx.navigation:navigation-compose")

    // Secure data store
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("com.google.code.gson:gson:2.8.9")

    // Coil
    implementation("io.coil-kt:coil-compose:2.4.0")

    // Extended icons
    implementation("androidx.compose.material:material-icons-extended:1.4.3")

}