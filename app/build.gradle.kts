import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

val properties = Properties()
properties.load(project.rootProject.file("secret.properties").inputStream())

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.vladabur.popcorn"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.vladabur.popcorn"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        create("release") {
            storeFile = file(properties.getProperty("RELEASE_STORE_FILE"))
            storePassword = properties.getProperty("RELEASE_STORE_PASSWORD")
            keyAlias = properties.getProperty("RELEASE_KEY_ALIAS")
            keyPassword = properties.getProperty("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    flavorDimensions.add("main")
    productFlavors {
        create("prod") {
            dimension = "main"
            buildConfigField("String", "BASE_URL", "\"https://api.themoviedb.org/3/\"")
            buildConfigField("String", "BASE_IMAGE_URL", "\"https://image.tmdb.org/t/p/original\"")
            buildConfigField("String", "SHARE_URL_PREFIX", "\"https://www.themoviedb.org/movie/\"")
            buildConfigField("String", "API_ACCESS_TOKEN", properties.getProperty("API_ACCESS_TOKEN"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    //DI
    implementation(libs.hilt.android)
    implementation(libs.androidx.runner)
    implementation(libs.androidx.junit.ktx)
    ksp(libs.hilt.android.compiler)

    //Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    
    // Retrofit
    implementation(libs.retrofit)

    //GSON
    implementation(libs.gson)
    implementation(libs.converter.gson)

    // OkHttp
    implementation(libs.logging.interceptor)
    
    //Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.kotlinx.serialization.json)

    //Paging
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)


    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.compiler)
    implementation(libs.androidx.room.paging)
    
    //Coil
    implementation(libs.coil.compose)
    
    //Compose
    implementation(libs.androidx.compose.material)
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    implementation(libs.androidx.core)
    testImplementation(libs.junit.junit)
    testImplementation(libs.hamcrest.all)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.robolectric)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.truth)
    testImplementation(libs.mockito.core)
}