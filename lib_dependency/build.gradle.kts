plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("com.github.dcendents.android-maven")
}

group = "com.github.bytebyte6"

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
    api(Libs.ANDROIDX_CORE_KTX)
    api(Libs.APPCOMPAT)
    api(Libs.MATERIAL_DESIGN)
    api(Libs.CONSTRAINTLAYOUT)
    api(Libs.RECYCLERVIEW)
    api(Libs.SWIPEREDRESHLAYOUT)
    api(Libs.GSON)
    api(Libs.LOTTIE)
    api(Libs.KOTLIN_COROUTINES)
    api(Libs.LIVEDATA_KTX)
    api(Libs.ACTIVITY)
    api(Libs.FRAGMENT)
    api(Libs.WORK_RUNTIME_KTX)
    api(Libs.RECYCLERVIEW_SELECTION)
    api(Libs.RXJAVA3)
    api(Libs.RETROFIT)
    api(Libs.RETROFIT_RXJAVA)
    api(Libs.RETROFIT_GSON)
    api(Libs.RETROFIT_INTERCEPTOR)
    api(Libs.KOIN)
    api(Libs.KOIN_SCPOE)
    api(Libs.KOIIN_VIEWMODEL)
    api(Libs.GLIDE)
    api(Libs.ROOM_RUNTIME)
    api(Libs.ROOM_KTX)
    api(Libs.LIFECYCLE_COMMON)
    kapt(Libs.KAPT_ROOM)
    kapt(Libs.KAPT_GLIDE)
}
