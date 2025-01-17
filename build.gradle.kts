buildscript {
    repositories {
        google()
        mavenCentral()
        // JitPack repositoryni qo'shish
        maven(url = "https://jitpack.io")
        jcenter()
    }
    dependencies {
        classpath(libs.google.services)
        classpath (libs.androidx.navigation.safe.args.gradle.plugin)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.google.dagger.hilt.android") version "2.50" apply false
    id("com.android.library") version "8.0.0" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
}