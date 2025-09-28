pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(uri("$rootDir/rn/node_modules/react-native/android"))
    }
}
include(":react-native-webview")
project(":react-native-webview").projectDir =
    File(rootDir, "rn/node_modules/react-native-webview/android")

rootProject.name = "My Application"
include(":app")

