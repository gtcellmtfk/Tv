plugins {
    id(Plugins.JAVA_LIB)
    id(Plugins.KOTLIN)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Libs.KOTLIN_STDLIB)
}