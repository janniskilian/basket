plugins {
    id("com.android.library")
    id("common-ui-module-plugin")
}

dependencies {
    implementation(project(":core-type"))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.viewmodel)
    implementation(Dependencies.AndroidX.datastorePref)
}
