import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
}

// Ambil properti dari file local.properties
val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

android {
    namespace = "com.yusufwdn.cryptowatcher"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.yusufwdn.cryptowatcher"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // Membuat field BuildConfig yang bisa diakses dari kode
        buildConfigField(
            "String",
            "COINGECKO_API_KEY",
            "\"${localProperties.getProperty("cgApiKey", "")}\""
        )
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
}

dependencies {

    // default
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Untuk mengambil data dari API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Untuk mengubah data JSON dari API menjadi object
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Untuk melihat log request API di Logcat
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Untuk menampilkan gambar dari URL
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Untuk menampilkan list data
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Untuk keperluan layouting
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.12.0")
}