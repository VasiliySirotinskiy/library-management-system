pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.android.application") version "7.4.2"
        id("org.jetbrains.kotlin.android") version "1.9.24"
        id("org.jetbrains.kotlin.kapt") version "1.9.24"
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TBank"
include(":app")
include(":common")
include(":data")
include(":domain")
include(":presentation")
