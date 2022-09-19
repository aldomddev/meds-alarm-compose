import Dependencies.Deps

plugins {
    id(Dependencies.BuildPlugins.androidApplication)
    id(Dependencies.BuildPlugins.daggerHiltAndroid)
    id(Dependencies.BuildPlugins.kotlinAndroid)
    id(Dependencies.BuildPlugins.kotlinKapt)
    id(Dependencies.BuildPlugins.kotlinParcelize)
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "br.com.amd.medsalarm"
    compileSdk = 33

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
        kotlinCompilerExtensionVersion = Versions.androidxComposeCompiler
    }
    packagingOptions {
        resources {
            excludes += mutableSetOf("/META-INF/{AL2.0,LGPL2.1}", "DebugProbesKt.bin")
        }
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(Deps.accompanistSystemUiController)
    implementation(Deps.androidMaterial)
    implementation(Deps.androidxAppCompat)
    implementation(Deps.androidxCore)
    implementation(Deps.workManager)
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.4.0")
    // for java.time support
    coreLibraryDesugaring(Deps.desugarJdkLibs)
    // date time non-official dialogs
    implementation(Deps.materialDialogsDateTime)
    // libs
    dependOnCompose()
    dependOnCoroutines()
    dependOnHilt()
    dependOnLifecycle()
    dependOnRoom()
    implementation(Deps.timber)
    // test
    dependOnTests()
}