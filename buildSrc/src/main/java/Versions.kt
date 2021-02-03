import java.text.SimpleDateFormat
import java.util.*

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

    @Suppress("SimpleDateFormat")
    fun date(): String {
        return SimpleDateFormat("yyyy-MM-dd").format(Date())
    }
}
