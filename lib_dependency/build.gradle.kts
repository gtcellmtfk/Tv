plugins {
    id(Plugins.LIB)
    id(Plugins.KOTLIN_ANDROID)
    id(Plugins.KOTLIN_ANDROID_EXT)
    id(Plugins.KOTLIN_KAPT)
    id(Plugins.DCENDENTS_ANDROID_MAVEN)
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
    api(Libs.ANDROIDX_CORE_KTX)
    api(Libs.MATERIAL_DESIGN)
    api(Libs.CONSTRAINTLAYOUT)
    api(Libs.RECYCLERVIEW)
    api(Libs.SWIPEREDRESHLAYOUT)
    api(Libs.GSON)
    api(Libs.LOTTIE)
    api(Libs.KOTLIN_COROUTINES)
    api(Libs.KOTLIN_STDLIB)
    api(JetPack.ANDROID_APP_COMPAT)
    api(JetPack.LIFECYCLE_COMMON)
    api(JetPack.LIFECYCLE_LIVEDATA_KTX)
    api(JetPack.ACTIVITY)
    api(JetPack.FRAGMENT)
    api(JetPack.WORK_RUNTIME_KTX)
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
}
