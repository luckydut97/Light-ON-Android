plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

// 소스 세트 설정 추가
sourceSets {
    main {
        java.srcDirs("src/main/java", "src/main/kotlin")
    }
    test {
        java.srcDirs("src/test/java", "src/test/kotlin")
    }
}

// 의존성 섹션 추가
dependencies {
    // 코틀린 표준 라이브러리
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // 테스트 관련 의존성
    testImplementation("junit:junit:4.13.2")
}