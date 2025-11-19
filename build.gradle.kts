// Root build.gradle.kts
// ═══════════════════════════════════════════════════════════════════════════
// A.u.r.a.K.a.I Reactive Intelligence - Root Build Configuration
// ═══════════════════════════════════════════════════════════════════════════

// Apply plugin version management to all projects
plugins {
    // Base plugins with versions - Using stable versions for production
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.0" apply false
    id("org.jetbrains.kotlin.plugin.parcelize") version "2.1.0" apply false

    // Android plugins - Using stable AGP version
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false

    // Other plugins
    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    id("com.google.devtools.ksp") version "2.1.0-1.0.29" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
}

// Clean task for the root project
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

// Configure all projects
allprojects {
    // Common configurations can go here
    group = "dev.aurakai.auraframefx"
    version = "0.1.0"

    // Make tests configurable via project property
    // Usage: ./gradlew test -PenableTests=false (to disable)
    // Or add 'enableTests=false' to gradle.properties to disable by default
    val enableTests = project.findProperty("enableTests")?.toString()?.toBoolean() ?: true

    if (!enableTests) {
        tasks.withType<AbstractTestTask> {
            enabled = false
        }

        tasks.matching { task ->
            task.name.startsWith("test") ||
            task.name.endsWith("Test") ||
            task.name.contains("androidTest")
        }.configureEach {
            enabled = false
        }
    }
}

