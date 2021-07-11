plugins {
    id("com.android.application")
    id("common-ui-module-plugin")
}

android {
    defaultConfig {
        applicationId = Versions.APPLICATION_ID
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isShrinkResources = true
        }
    }

    packagingOptions {
        resources.excludes.add("META-INF/LGPL2.1")
        resources.excludes.add("META-INF/AL2.0")
    }
}

dependencies {
    implementation(project(":core-type"))
    implementation(project(":core-util"))
    implementation(project(":core-ui"))
    implementation(project(":core-data"))
    implementation(project(":lists"))
    implementation(project(":groups"))
    implementation(project(":articles"))
    implementation(project(":categories"))

    implementation(Dependencies.AndroidX.core)
    implementation(Dependencies.AndroidX.appcompat)
    implementation(Dependencies.AndroidX.activity)
    implementation(Dependencies.AndroidX.viewmodel)
    implementation(Dependencies.AndroidX.preference)
    implementation(Dependencies.AndroidX.roomRuntime)
    implementation(Dependencies.AndroidX.roomKtx)
    implementation(Dependencies.AndroidX.datastorePref)
}
