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

    implementation(Dependencies.AndroidX.paging)

    implementation(Dependencies.AndroidX.roomRuntime)
    implementation(Dependencies.AndroidX.roomKtx)
    implementation(Dependencies.AndroidX.roomPaging)
    kapt(Dependencies.AndroidX.roomCompiler)
}
