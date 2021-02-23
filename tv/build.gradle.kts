import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id(Plugins.APP)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_ANDROID_EXT)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.FIREBASE_CRASH)
    id(Plugins.GOOGLE_SERVICES)
}

val key = loadProperties(projectDir.absolutePath + "/keystore.properties")

android {
    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.buildToolsVersion)

    signingConfigs {
        create("tv_release") {
            storeFile = file(key["storeFile"] as String)
            storePassword = key["storePassword"] as String
            keyAlias = key["keyAlias"] as String
            keyPassword = key["keyPassword"] as String
        }
    }

    defaultConfig {
        applicationId = "com.bytebyte6.rtmp"
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        setProperty("archivesBaseName", "tv-$versionName")

        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-proguard-rules.pro")
        signingConfig = signingConfigs.getByName("tv_release")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        maybeCreate("labtest")
        getByName("labtest") {
            initWith(getByName("release"))
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            versionNameSuffix = "-labtest"
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":lib_dependency"))
    implementation(project(":lib_recyclerviewutils"))
    implementation(project(":lib_common"))
    implementation(project(":tv_usecase"))
    implementation(project(":tv_model"))
    implementation(project(":tv_viewmodel"))
    kapt(Kapt.GLIDE)

    implementation(Libs.EXOPLAYER)
    implementation(Libs.EXOPLAYER_CORE)
    implementation(Libs.EXOPLAYER_DASH)
    implementation(Libs.EXOPLAYER_UI)
    implementation(Libs.EXOPLAYER_RTMP)

    implementation(Libs.FIREBASE_ANALYTICS)
    implementation(Libs.FIREBASE_CRASH)
    implementation(platform(Libs.FIREBASE_BOM))

    debugImplementation(DebugImpl.FRAGMENT_TESTING) {
        exclude("androidx.test", "core")
    }
    debugImplementation(Libs.LEAK_CANARY)

    androidTestImplementation(AndroidTest.ESPRESSO_CORE)
    androidTestImplementation(AndroidTest.ESPRESSO_CONTRIB)
    androidTestImplementation(AndroidTest.ESPRESSO_UI_AUTOMATOR)
    androidTestImplementation(AndroidTest.ESPRESSO_INTEGRATION)
    androidTestImplementation(AndroidTest.CORE)
    androidTestImplementation(AndroidTest.RUNNER)
    androidTestImplementation(AndroidTest.RULES)
    androidTestImplementation(AndroidTest.KOIN)
    androidTestImplementation(AndroidTest.ARCH_TESTING)
    androidTestImplementation(AndroidTest.JUNIT_EXT)

    testImplementation(Test.JUNIT)
}

configurations.all {
    resolutionStrategy {
        force(Libs.KOTLIN_STDLIB, Libs.KOTLIN_STDLIB_JDK)
    }
}