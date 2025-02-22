plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Secure API Key Fetching
        val apiKey: String? = project.findProperty("API_KEY") as String?
        buildConfigField("String", "API_KEY", "\"$apiKey\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField ("String", "SPOONACULAR_API_KEY", "\"6f85b5800520491e8e7a83d6a4d0b4ab\"")
        }
        release {
            buildConfigField ("String", "SPOONACULAR_API_KEYY", "\"6f85b5800520491e8e7a83d6a4d0b4ab\"")
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    // AndroidX Core Libraries
    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat.v161)
    implementation(libs.androidx.constraintlayout.v214)

    // Compose UI Dependencies
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.compose)

    // Material Design
    implementation(libs.material.v1100)

    // Image Processing (Glide)
    implementation(libs.glide.v4151)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.monitor)
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation ("com.google.firebase:firebase-auth-ktx:22.3.1")
    annotationProcessor(libs.compiler.v4151)

    // Networking (Retrofit) - REQUIRED for Spoonacular API
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // ViewModel & LiveData (For MVVM Architecture)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Circle Image View (If needed)
    implementation(libs.circleimageview)
    implementation (libs.material)

}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    implementation("com.google.android.exoplayer:exoplayer:2.18.5")
}


