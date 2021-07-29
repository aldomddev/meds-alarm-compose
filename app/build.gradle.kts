import Dependencies.Deps

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "br.com.amd.medsalarm"
        minSdk = 23
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose
    }
    packagingOptions {
        resources {
            excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}")//"/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.androidMaterial)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxCore)
    implementation(Deps.androidxLifecycleRuntime)
    // compose
    implementation(Deps.androidxActivityCompose)
    implementation(Deps.androidxComposeUi)
    implementation(Deps.androidxComposeMaterial)
    implementation(Deps.androidxComposeUiToolingPreview)
    implementation(Deps.androidxNavigationCompose)
    implementation(Deps.androidxHiltNavigationCompose)
    // di
    implementation(Deps.hiltAndroid)
    kapt(Deps.hiltAndroidCompiler)
    // debug
    debugImplementation(Deps.androidxComposeUiTooling)
    // test
    testImplementation(Deps.junit)
    androidTestImplementation(Deps.androidxJunit)
    androidTestImplementation(Deps.androidxEspresso)
    androidTestImplementation(Deps.androidxComposeJunit)
}