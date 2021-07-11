plugins {
    id("com.android.library")
    id("common-ui-module-plugin")
}

dependencies {
    implementation(project(":core-type"))
    implementation(project(":core-util"))
    implementation(project(":core-data"))
    implementation(project(":core-ui"))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.viewmodel)
}
