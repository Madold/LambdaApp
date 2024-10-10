plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.services)
    alias(libs.plugins.ksp)
    id(libs.plugins.dagger.hilt.plugin.get().pluginId)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.markusw.lambda"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.markusw.lambda"
        minSdk = 24
        targetSdk = 34
        versionCode = 3
        versionName = "3.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isShrinkResources = true
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
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.jetpack.loading)
    implementation(libs.com.google.firebase.crashlytics)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(platform(libs.firebase.bom))
    implementation(libs.com.google.dagger)
    ksp(libs.com.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.bundles.stream)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.com.google.firebase.auth)
    implementation(libs.com.google.android.gms.play.services.auth)
    implementation(libs.com.google.firebase.firestore)
    implementation(libs.com.google.firebase.storage)
    implementation(libs.app.cash.sqldelight.android.driver)
    implementation(libs.app.cash.sqldelight.coroutines.extensions)
    implementation(libs.io.coil.kt.coil.compose)
    implementation(libs.com.google.android.gms.play.services.ads)
    implementation(libs.androidx.core.splashscreen)

    //Test implementations
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

sqldelight {
    databases {
        create("LambdaDatabase") {
            packageName.set("com.markusw.lambda.db")
        }
    }
}