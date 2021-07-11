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
    implementation(Dependencies.AndroidX.fragment)
    implementation(Dependencies.AndroidX.livedata)
    implementation(Dependencies.AndroidX.viewmodel)
    implementation(Dependencies.AndroidX.constraintlayout)
    implementation(Dependencies.AndroidX.preference)
    implementation(Dependencies.AndroidX.roomRuntime)
    implementation(Dependencies.AndroidX.roomKtx)
    implementation(Dependencies.AndroidX.datastorePref)

    implementation(Dependencies.material)

    androidTestImplementation(Dependencies.AndroidTesting.espresso)
    androidTestImplementation(Dependencies.AndroidTesting.espressoContrib)
}
