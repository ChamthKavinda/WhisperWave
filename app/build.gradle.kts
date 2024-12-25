@file:Suppress("UNUSED_EXPRESSION")

plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.chama.whisperwave"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chama.whisperwave"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Scalable Size Unit (Support for different screen sizes)
    implementation(libs.intuit.sdp.android)
    implementation(libs.ssp.android)

    // Rounded ImageView
    implementation(libs.roundedimageview)

    // Firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.firestore)

    // MultiDex
    implementation(libs.multidex)
}