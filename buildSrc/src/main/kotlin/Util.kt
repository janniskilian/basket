import org.gradle.api.artifacts.dsl.DependencyHandler

fun commitCount(): Int {
    val process = Runtime.getRuntime().exec("git rev-list --all --count")
    val result = process.waitFor()
    if (result != 0) return 0

    return process
        .inputStream
        .reader()
        .useLines { line ->
            line.first().toInt()
        }
}

fun DependencyHandler.implementation(dependency: String) {
    add("implementation", dependency)
}

fun DependencyHandler.debugImplementation(dependency: String) {
    add("debugImplementation", dependency)
}

fun DependencyHandler.testImplementation(dependency: String) {
    add("testImplementation", dependency)
}

fun DependencyHandler.androidTestImplementation(dependency: String) {
    add("androidTestImplementation", dependency)
}

fun DependencyHandler.kapt(dependency: String) {
    add("kapt", dependency)
}

fun DependencyHandler.addCompose() {
    implementation(Dependencies.Compose.runtime)
    implementation(Dependencies.Compose.runtimeLivedata)
    implementation(Dependencies.Compose.foundation)
    implementation(Dependencies.Compose.ui)
    implementation(Dependencies.Compose.uiTooling)
    implementation(Dependencies.Compose.uiPreview)
    implementation(Dependencies.Compose.material)
    implementation(Dependencies.Compose.materialIconsCore)
    implementation(Dependencies.Compose.materialIconsExtended)
    implementation(Dependencies.Compose.accompanistInsets)

    debugImplementation(Dependencies.Compose.uiTestManifest)
    androidTestImplementation(Dependencies.Compose.uiTestJunit)

    implementation(Dependencies.AndroidX.viewmodel)
    implementation(Dependencies.AndroidX.navigation)
    implementation(Dependencies.AndroidX.hilt)
    implementation(Dependencies.AndroidX.activity)
    implementation(Dependencies.AndroidX.paging)
    implementation(Dependencies.AndroidX.constraintlayout)
}
