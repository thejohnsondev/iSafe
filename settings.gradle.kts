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

rootProject.name = "iSafe"
include(":app")
include(":feature:vault")
include(":core:common")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":core:datastore")
include(":core:designsystem")
include(":core:data")
include(":feature:addpassword")
include(":vault")
include(":passwordaddedit")
