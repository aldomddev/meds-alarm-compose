import Dependencies.Deps

plugins {
    id(Dependencies.BuildPlugins.androidApplication)
    id(Dependencies.BuildPlugins.kotlinAndroid)
    id(Dependencies.BuildPlugins.kotlinKapt)
    id(Dependencies.BuildPlugins.daggerHiltAndroid)
}

android {
    compileSdk = 31

    defaultConfig {
        applicationId = "br.com.amd.medsalarm"
        minSdk = 23
        targetSdk = compileSdk
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
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
    implementation(Deps.workManager)
    // for java.time support
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")
    // date time non-official dialogs
    implementation(Deps.materialDialogsDateTime)
    // libs
    dependOnCoroutines()
    dependOnLifecycle()
    dependOnCompose()
    dependOnHilt()
    dependOnRoom()
    // test
    dependOnTests()
}