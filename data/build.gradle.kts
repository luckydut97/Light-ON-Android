plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.library")
    alias(libs.plugins.ksp) // Room을 위한 KSP만 유지
}

android {
    namespace = "com.luckydut97.lighton.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Android Core
    implementation(libs.androidx.core.ktx)

    // Domain 모듈 의존성 추가
    implementation(project(":domain"))

    // 네트워크 통신 라이브러리
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Room Database (KSP 사용)
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // 테스트 의존성
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
}
