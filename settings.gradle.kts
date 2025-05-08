pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        // JetBrains Compose 저장소 제거
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // JetBrains Compose 저장소 제거
    }
}

rootProject.name = "lighton"
include(":app")
include(":core")
include(":data")
include(":feature-auth")
include(":feature-home")
include(":feature-map")