plugins {
    id(Plugins.LIB)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_ANDROID_EXT)
    id(Plugins.KOTLIN_KAPT)
}

android {
    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.buildToolsVersion)

    defaultConfig {
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName


        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-proguard-rules.pro")
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
}

dependencies {
    implementation(project(":lib_dependency"))
    implementation(project(":lib_common"))
    implementation(project(":tv_usecase"))
    implementation(project(":tv_model"))
    implementation(Libs.EXOPLAYER_CORE)

    androidTestImplementation(AndroidTest.CORE)
    androidTestImplementation(AndroidTest.RUNNER)
    androidTestImplementation(AndroidTest.RULES)
    androidTestImplementation(AndroidTest.KOIN)
    androidTestImplementation(AndroidTest.ARCH_TESTING)
    androidTestImplementation(AndroidTest.JUNIT_EXT)
    androidTestImplementation(project(":lib_test"))

    testImplementation(project(":lib_test"))
    testImplementation(Test.JUNIT)
    testImplementation(Libs.GETIMAGE)
    testImplementation(project(":tv_test"))
}