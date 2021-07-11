plugins {
    id("com.android.library")
    id("common-ui-module-plugin")
}

dependencies {
    implementation(project(":core-type"))
    implementation(project(":core-util"))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.viewmodel)
}
