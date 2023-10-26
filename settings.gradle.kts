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

include(":core:common")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":core:datastore")
include(":core:designsystem")

include(":vault:presentation")
include(":vault:domain")
include(":vault:data")
include(":passwordaddedit:presentation")
include(":passwordaddedit:domain")
