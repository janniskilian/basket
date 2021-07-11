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
    implementation(Dependencies.AndroidX.activity)
    implementation(Dependencies.AndroidX.fragment)
    implementation(Dependencies.AndroidX.livedata)
    implementation(Dependencies.AndroidX.viewmodel)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.AndroidX.navFragment)

    implementation(Dependencies.material)
}
