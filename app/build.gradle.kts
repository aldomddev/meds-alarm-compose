import Dependencies.Deps

plugins {
    id(Dependencies.BuildPlugins.androidApplication)
    id(Dependencies.BuildPlugins.kotlinAndroid)
    id(Dependencies.BuildPlugins.kotlinKapt)
    id(Dependencies.BuildPlugins.daggerHiltAndroid)
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
            excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}", "DebugProbesKt.bin")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.androidMaterial)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxCore)
    // libs
    dependOnCoroutines()
    dependOnLifecycle()
    dependOnCompose()
    dependOnHilt()
    dependOnRoom()
    // test
    dependOnTests()
}