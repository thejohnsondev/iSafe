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
include(":core:ui")
include(":core:datastore")
include(":core:designsystem")

include(":vault:presentation")
include(":vault:domain")
include(":vault:data")
include(":passwordaddedit:presentation")
include(":passwordaddedit:domain")
include(":auth:presentation")
include(":auth:domain")
include(":auth:data")
include(":notes:data")
include(":notes:domain")
include(":notes:presentation")
include(":settings:presentation")
include(":settings:domain")
include(":core:network")
include(":core:database")
