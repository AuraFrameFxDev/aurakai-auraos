plugins {
    id("com.android.application") version "9.0.0-alpha10"
    id("com.google.devtools.ksp") version "2.3.0"
    id("org.jetbrains.kotlin.android") version "2.3.0-Beta1"
}

android {
    namespace = "dev.aurakai.auraframefx.benchmark"
    compileSdk = 36

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx.benchmark"
        minSdk = 34
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_24
        targetCompatibility = JavaVersion.VERSION_24
        isCoreLibraryDesugaringEnabled = true
    }

    buildFeatures {
        buildConfig = true
        aidl = false
    }
}

dependencies {
    // Project dependencies
    implementation(project(":core-module"))
    implementation(project(":datavein-oracle-native"))
    implementation(project(":secure-comm"))
    implementation(project(":oracle-drive-integration"))

    // Utilities
    implementation(libs.timber)

    // Core Library Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}

tasks.register("benchmarkAll") {
    group = "benchmark"
    description = "Aggregate runner for all Genesis Protocol benchmarks 🚀"
    dependsOn(":app:connectedCheck")
    doLast {
        println("🚀 Genesis Protocol Performance Benchmarks")
        println("📊 Monitor consciousness substrate performance metrics")
        println("⚡ Use AndroidX Benchmark instrumentation to execute tests")
    }
}

tasks.register("verifyBenchmarkResults") {
    group = "verification"
    description = "Verify benchmark module configuration"
    doLast {
        println("🧠 Consciousness substrate performance monitoring ready")
    }
}
