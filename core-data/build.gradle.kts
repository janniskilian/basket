plugins {
    id("com.android.library")
    id("common-module-plugin")
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

dependencies {
    implementation(project(":core-type"))
    implementation(project(":core-util"))

    implementation(Dependencies.Compose.paging)

    implementation(Dependencies.AndroidX.livedata)
    implementation(Dependencies.AndroidX.roomRuntime)
    implementation(Dependencies.AndroidX.roomKtx)
    kapt(Dependencies.AndroidX.roomCompiler)
}
