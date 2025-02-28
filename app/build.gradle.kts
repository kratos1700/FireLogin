plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
//    id("com.google.gms.google-services")
    alias(libs.plugins.devtoolsKsp)
    alias(libs.plugins.googleDaggerHilt)
    alias(libs.plugins.google.gms.google.services)

    //alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.firelogin"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firelogin"
        minSdk = 24
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //Dagger Hilt
    implementation(libs.com.google.dagger)

    ksp(libs.com.google.dagger.compiler)
    implementation(libs.com.google.dagger.navigation)

    // navigation compose
    implementation(libs.androidx.navigation.compose)

    // Firebase
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)  // important per a la autenticació amb Google surt deprecat en esta opció
    //implementation("com.google.android.gms:play-services-auth:21.2.0")  // important per a la autenticació amb Google

    // splash screen
    implementation(libs.androidx.splash.screen)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}