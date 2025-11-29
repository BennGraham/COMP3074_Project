plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.example.comp3074_project"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.comp3074_project"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
    implementation(libs.play.services.maps)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // room dependencies

    // room components
    implementation("androidx.room:room-runtime:2.8.3")
    annotationProcessor("androidx.room:room-compiler:2.8.3")

    // lifecycle components
    // viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    // livedata
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.4")
    // lifecycles only (without viewmodel or livedata)
    implementation("androidx.lifecycle:lifecycle-runtime:2.9.4")
    // saved state module for viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.9.4")
    // annotation processor
    implementation("androidx.lifecycle:lifecycle-common-java8:2.9.4")
}