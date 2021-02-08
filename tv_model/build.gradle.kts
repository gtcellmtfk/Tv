plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
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

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
                arguments["room.schemaLocation"] = "$projectDir/schemas"
                arguments["room.expandProjection"] = "false"
            }
        }
    }

    buildTypes {
        getByName("release") {
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
}

dependencies {
    implementation(project(":lib_dependency"))
    implementation(project(":lib_common"))

    kapt(Libs.KAPT_ROOM)

    androidTestImplementation(Libs.TEST_CORE)
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.TEST_RULES)
    androidTestImplementation(Libs.TEST_KOIN)
    androidTestImplementation(Libs.TEST_ARCH_TESTING)
    androidTestImplementation(Libs.TEST_JUNIT_EXT)
    androidTestImplementation(Libs.TEST_JUNIT_KTX)
    androidTestImplementation(project(":tv_test"))

    testImplementation(Libs.TEST_JUNIT)

}