import java.util.Locale

object Dependencies {

    object BuildPlugins {
        const val android = "android"

        const val androidApplication = "com.android.application"
        const val androidExtensions = "android.extensions"
        const val androidLibrary = "com.android.library"

        const val kotlinAndroid = "android"
        const val kotlinAndroidExtensions = "kotlin-android-extensions"
        const val kotlinKapt = "kapt"

        const val navigationSageArgs = "androidx.navigation.safeargs.kotlin"

        const val daggerHiltAndroid = "dagger.hilt.android.plugin"
        const val daggerHiltAndroidGradle =
            "com.google.dagger:hilt-android-gradle-plugin:${Versions.hiltAndroid}"

        const val gradleVersionsPlugin = "com.github.ben-manes.versions"
        const val gradleVersionsClasspath =
            "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
    }

    object Deps {
        const val androidMaterial = "com.google.android.material:material:${Versions.androidMaterial}"

        const val androidxActivityCompose = "androidx.activity:activity-compose:${Versions.androidxActivityCompose}"
        const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompat}"
        const val androidxComposeMaterial = "androidx.compose.material:material:${Versions.androidxComposeMaterial}"
        const val androidxComposeUi = "androidx.compose.ui:ui:${Versions.androidxComposeUi}"
        const val androidxComposeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.androidxComposeUiTooling}"
        const val androidxComposeUiToolingPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.androidxComposeUiToolingPreview}"
        const val androidxCore = "androidx.core:core-ktx:${Versions.androidxCore}"
        const val androidxHiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.androidxHiltNavigationCompose}"
        const val androidxLifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycleRuntime}"
        const val androidxNavigationCompose = "androidx.navigation:navigation-compose:${Versions.androidxNavigationCompose}"

        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hiltAndroid}"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hiltAndroid}"

        // Tests
        const val androidxJunit = "androidx.test.ext:junit:${Versions.androidxJunit}"
        const val androidxEspresso = "androidx.test.espresso:espresso-core:${Versions.androidxEspresso}"
        const val androidxComposeJunit = "androidx.compose.ui:ui-test-junit4:${Versions.androidxComposeJunit}"

        const val junit = "junit:junit:${Versions.junit}"
    }

    @JvmStatic
    fun isNonStable(version: String): Boolean {
        val stableKeyword =
            listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase(Locale.ROOT).contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }
}