buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(ClassPath.FIREBASE_CRASH_GRADLE)
        classpath(ClassPath.GOOGLE_SERVICE)
        classpath(ClassPath.KOTLIN_GRADLE)
        classpath(ClassPath.PULISH_GRADLE)
        classpath(ClassPath.TOOLS_GRADLE)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri(RepoUri.JITPACK_IO) }
        maven { url = uri(RepoUri.ALI_YUN_MAVEN) }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}