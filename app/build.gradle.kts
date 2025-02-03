import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinXSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.sqlDelight)
}

android {
    namespace = "com.humanforce.humanforceandroidengineeringchallenge"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.humanforce.humanforceandroidengineeringchallenge"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val envFile = rootProject.file(".env")
        val properties = Properties()
        if (envFile.exists()) {
            properties.load(envFile.inputStream())
        }

        buildConfigField("String", "OPEN_WEATHER_MAP_API_KEY", properties.getProperty("OPEN_WEATHER_MAP_API_KEY", "\"\""))
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.ui.text.google.fonts)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose
    implementation(platform(libs.composeBom))
    implementation(libs.bundles.compose)
    implementation(libs.composeUiToolingPreview)
    debugImplementation(libs.composeUiTooling)

    // Network
    implementation(libs.bundles.networking)

    // Dependency injection (Hilt)
    implementation(libs.bundles.hilt)
    ksp(libs.hiltCompiler)

    // Navigation
    implementation(libs.bundles.navigation)

    // DataStore
    implementation(libs.dataStore)

    // Java 8+/Time Utils
    coreLibraryDesugaring(libs.desugar)
    implementation(libs.kotlinXDateTime)

    // SQLDelight
    implementation(libs.bundles.sqlDelight)

    implementation(libs.accompanistPermissions)
    implementation(libs.playServicesLocation)
}

sqldelight {
    databases {
        create("SavedLocationDatabase") {
            packageName.set("com.humanforce.humanforceandroidengineeringchallenge.data.db")
            schemaOutputDirectory.set(file("src/main/sqldelight/databases"))

        }
    }
}