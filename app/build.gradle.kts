import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android") version "2.57" // ✅ Match with dependency
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

hilt {
    enableAggregatingTask = false
}

kapt {
    correctErrorTypes = true
}

// Load local.properties securely
val localProperties = Properties().apply {
    val localFile = rootProject.file("local.properties")
    if (localFile.exists()) {
        localFile.inputStream().use { load(it) }
    }
}

// Retrieve API Key from local.properties
val apiKey: String = localProperties.getProperty("MY_SECRET_API_KEY") ?: "MISSING_API_KEY"



android {
    namespace = "com.malikstudios.backtune"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.malikstudios.backtune"
        minSdk = 24
        targetSdk = 35
        versionCode = 5
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        // Inject API Key as BuildConfig field
        buildConfigField("String", "MY_SECRET_API_KEY", "\"$apiKey\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug") // TODO: Replace with release key
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Firebase
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.config.ktx)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.messaging)

    // Google Play Services
    implementation(libs.play.services.measurement.api)
    // Hilt (Dependency Injection)
    implementation("com.google.dagger:hilt-android:2.57")
    implementation(libs.material3)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.ui.geometry)
    kapt("com.google.dagger:hilt-compiler:2.57")

    // Room (Database)
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    kapt("androidx.room:room-compiler:2.7.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


    // YouTube and Media
    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")
    implementation("androidx.media3:media3-exoplayer:1.2.1")
    implementation("androidx.media3:media3-ui:1.2.1")

    // UI and Graphics
    implementation("com.google.android.material:material:1.11.0")
    implementation("io.coil-kt:coil-compose:2.4.0")

    // OneSignal (Push Notifications)
    implementation("com.onesignal:OneSignal:[5.1.6, 5.1.99]")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debugging
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // YouTube, ExoPlayer, Material, Others
    implementation("com.squareup:javapoet:1.13.0")

    // Gson for JSON parsing
    implementation("com.google.code.gson:gson:2.8.8")
    //https://www.youtube.com/oembed?url=https://youtu.be/1Co4cTcDTyQ?si=NXhjhUymGOhfl6ji&format=json
}
