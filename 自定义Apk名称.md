# Kotlin DSL 自定义Apk输出名称

app build.gradle.kts

    defaultConfig {
      setProperty("archivesBaseName", "tv-$versionName-$versionCode-${Versions.date()}")
    }

Versions.kt

    //don't do that , will be throw file not found exception
    SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date())
    
    object Versions {
        @Suppress("SimpleDateFormat")
        fun date(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }