# 添加buildSrc文件夹

在项目根目录添加buildSrc文件夹

![image-20210203214349491](/pic/buildSrc.png)

在buildSrc文件夹内新建

build.gradle.kts

```kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}
```

到这完成了一半

# 修改setting.gradle为setting.gradle.kts

setting.gradle.kts

```kotlin
include(
	//修改为你的内容
    ":tv",
    ":tv_model"
)
rootProject.name = "Tv"
```

# 回到buildSrc文件夹创建Versions.kt和Libs.kt

Versions..Kt

```kotlin
//在此定义项目的版本号
object Versions {
    const val versionName = "1.0"
    const val versionCode = 1
    const val compile_sdk = 29
    const val target_sdk = 29
    const val min_sdk = 21
    const val buildToolsVersion = "30.0.2"
    const val android_gradle_plugin = "4.1.1"
    const val kotlin = "1.3.72"
    const val rxjava = "3.0.4"
    const val retrofit = "2.9.0"
    const val extJunit = "1.1.2"
    const val rules = "1.2.0"
    const val lifecycle_version = "2.2.0"
    const val androidx_test_core = "1.3.0"
    const val runner = "1.3.0"
    const val koin = "2.2.1"
    const val testing = "2.1.0"
    const val junit = "4.13.1"
    const val espresso = "3.3.0"
    const val room = "2.2.6"
    const val exoplayer = "2.12.2"
    const val glide = "4.11.0"
    const val fragment_test = "1.2.5"
}
```

Libs.kt

```kotlin
//依赖项
object Libs {
    const val TEST_CORE = "androidx.test:core:" + Versions.androidx_test_core
    const val TEST_RUNNER = "androidx.test:runner:" + Versions.runner
    const val TEST_RULES = "androidx.test:rules:" + Versions.rules
    const val TEST_KOIN = "org.koin:koin-test:" + Versions.koin
    const val TEST_ARCH_TESTING = "androidx.arch.core:core-testing:" + Versions.testing
    const val TEST_JUNIT_EXT = "androidx.test.ext:junit:" + Versions.extJunit
    const val TEST_JUNIT_KTX = "androidx.test.ext:junit-ktx:" + Versions.extJunit
    const val TEST_ESPRESSO_CORE = "androidx.test.espresso:espresso-core:" + Versions.espresso
    const val TEST_ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:" + Versions.espresso
    const val TEST_ESPRESSO_UI_AUTOMATOR = "androidx.test.uiautomator:uiautomator:2.2.0"
    const val TEST_ESPRESSO_INTEGRATION = "org.hamcrest:hamcrest-integration:1.3"
    const val TEST_JUNIT = "junit:junit:" + Versions.junit
    const val TEST_ROBOLECTRIC = "org.robolectric:robolectric:4.4"
    const val TEST_FRAGMENT_TESTING =
        "androidx.fragment:fragment-testing:${Versions.fragment_test}"

    //kapt
    const val KAPT_GLIDE = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val KAPT_ROOM = "androidx.room:room-compiler:${Versions.room}"

    //exoplayer
    const val EXOPLAYER = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
    const val EXOPLAYER_CORE = "com.google.android.exoplayer:exoplayer-core:${Versions.exoplayer}"
    const val EXOPLAYER_DASH = "com.google.android.exoplayer:exoplayer-dash:${Versions.exoplayer}"
    const val EXOPLAYER_UI = "com.google.android.exoplayer:exoplayer-ui:${Versions.exoplayer}"

    //androidx
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:1.3.2"
    const val APPCOMPAT = "androidx.appcompat:appcompat:1.2.0"
    const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:2.0.4"
    const val RECYCLERVIEW = "androidx.recyclerview:recyclerview:1.2.0-alpha05"
    const val SWIPEREDRESHLAYOUT = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    const val LIVEDATA_KTX = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0"
    const val ACTIVITY = "androidx.activity:activity:1.2.0-alpha07"
    const val FRAGMENT = "androidx.fragment:fragment:1.3.0-alpha07"
    const val WORK_RUNTIME_KTX = "androidx.work:work-runtime-ktx:2.4.0"
    const val RECYCLERVIEW_SELECTION = "androidx.recyclerview:recyclerview-selection:1.0.0"
    const val LIFECYCLE_COMMON =
        "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle_version}"

    const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:2.6"
    const val CRASH_REPORTER = "com.balsikandar.android:crashreporter:1.1.0"
    const val MATERIAL_DESIGN = "com.google.android.material:material:1.3.0-beta01"
    const val GSON = "com.google.code.gson:gson:2.8.6"
    const val LOTTIE = "com.airbnb.android:lottie:3.4.1"
    const val RXJAVA3 = "io.reactivex.rxjava3:rxjava:${Versions.rxjava}"

    //retrofit
    const val RETROFIT = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val RETROFIT_RXJAVA = "com.squareup.retrofit2:adapter-rxjava3:${Versions.retrofit}"
    const val RETROFIT_GSON = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val RETROFIT_INTERCEPTOR = "com.squareup.okhttp3:logging-interceptor:4.7.2"

    //koin
    const val KOIN = "org.koin:koin-android:${Versions.koin}"
    const val KOIN_SCPOE = "org.koin:koin-android-scope:${Versions.koin}"
    const val KOIIN_VIEWMODEL = "org.koin:koin-android-viewmodel:${Versions.koin}"

    //glide
    const val GLIDE = "com.github.bumptech.glide:glide:${Versions.glide}"

    //room
    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.room}"
    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.room}"

    //kotlin
    const val KOTLIN_STDLIB = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val KOTLIN_COROUTINES = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.1"

    const val GETIMAGE = "com.github.bytebyte6:GetImage:5.0"
}
```

# 最后修改module的build.gradle为build.gradle.kts

```kotlin
plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Versions.compile_sdk)
    buildToolsVersion(Versions.buildToolsVersion)

    signingConfigs {
        create("release") {
            storeFile = file("you file path")
            storePassword = ""
            keyAlias = ""
            keyPassword = ""
        }
    }

    defaultConfig {
        applicationId = ""
        minSdkVersion(Versions.min_sdk)
        targetSdkVersion(Versions.target_sdk)
        versionCode = Versions.versionCode
        versionName = Versions.versionName
        
        testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        consumerProguardFiles("consumer-proguard-rules.pro")
        signingConfig = signingConfigs.getByName("release")
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
    implementation(project(":tv_model"))

    kapt(Libs.KAPT_GLIDE)

    implementation(Libs.EXOPLAYER)
    implementation(Libs.EXOPLAYER_CORE)
    implementation(Libs.EXOPLAYER_DASH)
    implementation(Libs.EXOPLAYER_UI)

    debugImplementation(Libs.TEST_FRAGMENT_TESTING) {
        exclude("androidx.test", "core")
    }
    debugImplementation(Libs.LEAK_CANARY)
    debugImplementation(Libs.CRASH_REPORTER)

    androidTestImplementation(Libs.TEST_ESPRESSO_CORE)
    androidTestImplementation(Libs.TEST_ESPRESSO_CONTRIB)
    androidTestImplementation(Libs.TEST_ESPRESSO_UI_AUTOMATOR)
    androidTestImplementation(Libs.TEST_ESPRESSO_INTEGRATION)
    androidTestImplementation(Libs.TEST_CORE)
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.TEST_RULES)
    androidTestImplementation(Libs.TEST_KOIN)
    androidTestImplementation(Libs.TEST_ARCH_TESTING)
    androidTestImplementation(Libs.TEST_JUNIT)
    androidTestImplementation(Libs.TEST_JUNIT_KTX)

    testImplementation(Libs.TEST_JUNIT)
}
```

# Sync大功告成

