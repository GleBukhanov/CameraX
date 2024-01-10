plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.camerax"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.camerax"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding =true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("androidx.camera:camera-video:1.3.1")
    implementation("androidx.camera:camera-extensions:1.3.1")
    dependencies {
        val nav_version = "2.7.5"

        // Kotlin
        implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
        implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

        // Feature module Support
        implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

        // Testing Navigation
        androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

        // Jetpack Compose Integration
        implementation("androidx.navigation:navigation-compose:$nav_version")
    }

}