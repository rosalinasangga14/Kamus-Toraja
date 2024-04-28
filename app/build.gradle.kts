plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id 'kotlin-android'
    id 'kotlin-kapt'
}

android {
    namespace = "com.example.kamustoraya"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kamustoraya"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a', 'x86', 'x86_64'
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
}

dependencies {
    implementation platform("com.google.firebase:firebase-bom:32.6.0")
    implementation 'com.google.firebase:firebase-analytics'
    implementation 'com.google.firebase:firebase-database'
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2'
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel:2.4.0"
    implementation "androidx.lifecycle:lifecycle-livedata:2.4.0"
    implementation "androidx.activity:activity-ktx:1.3.1"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0"
    classpath 'com.google.gms:google-services:4.3.8'
}
