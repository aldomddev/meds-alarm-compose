// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

apply(plugin = Dependencies.BuildPlugins.gradleVersionsPlugin)

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.1.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")

        classpath(Dependencies.BuildPlugins.daggerHiltAndroidGradle)
        classpath(Dependencies.BuildPlugins.gradleVersionsClasspath)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// run $ ./gradlew dependencyUpdates
// run $ ./gradlew dependencyUpdates --refresh-dependencies --stacktrace --debug --scan
//
// with (optional):
//
// --refresh-dependencies option to refresh the cache (i.e. fetch the new releases/versions of the dependencies)
// --stacktrace option to get the stack trace.
// --info or --debug option to get more log output.
// --scan to get full insights.
tasks.named("dependencyUpdates", DependencyUpdatesTask::class.java).configure {

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "html"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"

    gradleReleaseChannel = "current"
    rejectVersionIf {
        Dependencies.isNonStable(candidate.version)
    }
}