// Top-level build file
import org.gradle.api.tasks.compile.JavaCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}

subprojects {
    // Force Java 17 for Java compilation everywhere
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    // Force Kotlin JVM target 17 everywhere
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
        }
    }

    plugins.withId("org.jetbrains.kotlin.android") {
        the<org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension>().apply {
            jvmToolchain(17)
        }
    }
    // 🔒 Apply to ALL configurations (compile + runtime, all variants)
    configurations.configureEach {
        // 1) Hard substitution (old -> new artifact)
        resolutionStrategy.dependencySubstitution {
            substitute(module("com.facebook.react:react-native"))
                .using(module("com.facebook.react:react-android:0.74.3"))
        }
        // 2) Also catch dynamic/rich variants that slip through
        resolutionStrategy.eachDependency {
            if (requested.group == "com.facebook.react" && requested.name == "react-native") {
                useTarget("com.facebook.react:react-android:0.74.3")
                because("React Native renamed Android artifact in 0.71+")
            }
        }
    }
}
