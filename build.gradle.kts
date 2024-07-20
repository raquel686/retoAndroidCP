// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript{

    dependencies {
        classpath ("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id ("com.android.library")  version "7.3.1" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false

}