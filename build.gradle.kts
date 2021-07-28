// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    //apply(from = "../versions.gradle.kts")
    //val hiltVersionPlugin: String by extra
    //println("AMD - $hiltVersionPlugin")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")

        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}