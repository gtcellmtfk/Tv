object AndroidTest {
    const val CORE = "androidx.test:core:" + Versions.androidx_test_core
    const val RUNNER = "androidx.test:runner:" + Versions.runner
    const val RULES = "androidx.test:rules:" + Versions.rules
    const val KOIN = "org.koin:koin-test:" + Versions.koin
    const val ARCH_TESTING = "androidx.arch.core:core-testing:" + Versions.testing

    //espresso
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:" + Versions.espresso
    const val ESPRESSO_CONTRIB = "androidx.test.espresso:espresso-contrib:" + Versions.espresso
    const val ESPRESSO_UI_AUTOMATOR = "androidx.test.uiautomator:uiautomator:2.2.0"
    const val ESPRESSO_INTEGRATION = "org.hamcrest:hamcrest-integration:1.3"

    //robolectric
    const val ROBOLECTRIC = "org.robolectric:robolectric:4.4"

    const val PAGING_COMMON = "androidx.paging:paging-common:${Versions.paging_version}"

    const val WORK_TESTING = "androidx.work:work-testing:${Versions.work_version}"

    const val JUNIT_EXT = "androidx.test.ext:junit:" + Versions.extJunit
    const val JUNIT_KTX = "androidx.test.ext:junit-ktx:" + Versions.extJunit
}