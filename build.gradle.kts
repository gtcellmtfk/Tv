buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(ClassPath.FIREBASE_CRASH)
        classpath(ClassPath.GOOGLE_SERVICE)
        classpath(ClassPath.GRADLE_KOTLIN)
        classpath(ClassPath.GRADLE_MAVEN)
        classpath(ClassPath.GRADLE)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri(Uri.JITPACK_IO) }
        maven { url = uri(Uri.ALIYUN_MAVEN) }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}