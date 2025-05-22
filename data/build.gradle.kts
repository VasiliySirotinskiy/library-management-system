plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    namespace   = "com.example.librarymanagementsystem.data"
    compileSdk  = 35

    defaultConfig {
        minSdk       = 24
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(libs.javax.inject)
    implementation(libs.androidx.room.runtime.v261)
    implementation(libs.androidx.room.ktx.v261)
    kapt(libs.androidx.room.compiler.v261)
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)
    implementation(libs.logging.interceptor)
    implementation(libs.moshi.kotlin)
    testImplementation           (libs.junit)
    androidTestImplementation    (libs.androidx.junit)
    androidTestImplementation    (libs.androidx.espresso.core)
}
