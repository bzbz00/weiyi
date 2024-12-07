plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.app_02"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.app_02"
        minSdk = 28
        targetSdk = 34
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
}

dependencies {
    implementation(libs.constraintlayout)
    implementation(libs.appcompat)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.google.material)
    implementation(libs.volley)
    implementation(libs.biometric)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.material)
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("org.greenrobot:eventbus:3.3.1")
}
