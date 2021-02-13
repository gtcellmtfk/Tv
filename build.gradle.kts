buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.5.0")
        classpath("com.google.gms:google-services:4.3.5")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.android.tools.build:gradle:${Versions.android_gradle_plugin}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("http://maven.aliyun.com/nexus/content/groups/public/") }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}