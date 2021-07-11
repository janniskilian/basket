object Dependencies {

    object Kotlin {

        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.KOTLIN}"

        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"

        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"

        const val coroutinesTest =
            "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
    }

    object AndroidX {

        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.0"
        const val activity = "androidx.activity:activity-ktx:1.3.0-rc01"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.5"
        const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
        const val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.NAV}"
        const val roomRuntime = "androidx.room:room-runtime:${Versions.ROOM}"
        const val roomKtx = "androidx.room:room-ktx:${Versions.ROOM}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM}"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"
        const val datastorePref = "androidx.datastore:datastore-preferences:1.0.0-rc01"
        const val preference = "androidx.preference:preference-ktx:1.1.1"
    }

    object Compose {

        const val runtime = "androidx.compose.runtime:runtime:${Versions.COMPOSE}"

        const val runtimeLivedata = "androidx.compose.runtime:runtime-livedata:${Versions.COMPOSE}"

        const val foundation = "androidx.compose.foundation:foundation:${Versions.COMPOSE}"

        const val ui = "androidx.compose.ui:ui:${Versions.COMPOSE}"

        const val uiTooling = "androidx.compose.ui:ui-tooling:1.0.0-beta09"

        const val uiPreview = "androidx.compose.ui:ui-tooling-preview:${Versions.COMPOSE}"

        const val material = "androidx.compose.material:material:${Versions.COMPOSE}"

        const val materialIconsCore =
            "androidx.compose.material:material-icons-core:${Versions.COMPOSE}"

        const val materialIconsExtended =
            "androidx.compose.material:material-icons-extended:${Versions.COMPOSE}"

        const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07"

        const val navigation = "androidx.navigation:navigation-compose:2.4.0-alpha04"

        const val hilt = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha03"

        const val activity = "androidx.activity:activity-compose:1.3.0-rc01"

        const val paging = "androidx.paging:paging-compose:1.0.0-alpha11"

        const val constraintlayout =
            "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha08"

        const val accompanistInsets =
            "com.google.accompanist:accompanist-insets:${Versions.ACCOMPANIST}"
    }

    object Hilt {

        const val android = "com.google.dagger:hilt-android:${Versions.HILT}"
        const val compiler = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
        const val testing = "com.google.dagger:hilt-android-testing:${Versions.HILT}"
        const val androidXCompiler = "androidx.hilt:hilt-compiler:${Versions.HILT_ANDROID_X}"
    }

    const val material = "com.google.android.material:material:1.3.0"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.5"

    object Testing {

        const val junit = "junit:junit:4.13.2"
        const val kotlinTest = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}"
        const val mockito = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0"
    }

    object AndroidTesting {

        const val core = "androidx.test:core:1.4.0"
        const val runner = "androidx.test:runner:1.4.0"
        const val rules = "androidx.test:rules:1.4.0"
        const val junit = "androidx.test.ext:junit:1.1.1"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
        const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"
        const val livedata = "androidx.arch.core:core-testing:2.1.0"
        const val room = "androidx.room:room-testing:${Versions.ROOM}"
        const val hilt = "com.google.dagger:hilt-android-testing:${Versions.HILT}"
        const val hiltCompiler = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
        const val paging = "androidx.paging:paging-common:3.0.0"
        const val barista = "com.schibsted.spain:barista:3.9.0"
    }

    object GradlePlugins {

        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
        const val android = "com.android.tools.build:gradle:7.1.0-alpha03"
        const val navSafeArgs =
            "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAV}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:2.37"
    }
}
