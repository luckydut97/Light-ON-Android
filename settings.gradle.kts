pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "lighton"
include(":app")
include(":core")
include(":data")
include(":domain")
include(":feature-auth")
include(":feature-home")
include(":feature-map")
include(":feature-stage")
include(":feature-mypage")
