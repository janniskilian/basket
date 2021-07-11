import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

open class CommonModulePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        with(project.plugins) {
            apply("kotlin-android")
            apply("kotlin-kapt")
            apply("kotlin-parcelize")
            apply("dagger.hilt.android.plugin")
        }

        with(project.extensions["kapt"] as KaptExtension) {
            correctErrorTypes = true
        }

        with(project.extensions["android"] as BaseExtension) {
            buildToolsVersion = Versions.BUILD_TOOLS
            compileSdkVersion(30)

            defaultConfig {
                minSdkVersion(21)
                targetSdkVersion(30)
                versionCode = Versions.VERSION_CODE
                versionName = Versions.VERSION_NAME
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                getByName("debug") {
                    multiDexEnabled = true
                }
                getByName("release") {
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro"
                    )
                }

                project.tasks.withType(KotlinCompile::class.java).configureEach {
                    kotlinOptions {
                        jvmTarget = JavaVersion.VERSION_1_8.toString()
                        freeCompilerArgs = freeCompilerArgs + listOf(
                            "-XXLanguage:+InlineClasses"
                        )
                    }
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_1_8
                    targetCompatibility = JavaVersion.VERSION_1_8
                }

                testOptions {
                    unitTests {
                        isIncludeAndroidResources = true
                    }

                    animationsDisabled = true
                }
            }

            project.dependencies {
                implementation(Dependencies.Kotlin.stdlib)
                implementation(Dependencies.Kotlin.coroutines)
                implementation(Dependencies.Kotlin.coroutinesAndroid)

                implementation(Dependencies.Hilt.android)
                kapt(Dependencies.Hilt.compiler)
                kapt(Dependencies.Hilt.androidXCompiler)

                implementation(Dependencies.timber)

                testImplementation(Dependencies.Testing.junit)
                testImplementation(Dependencies.Testing.kotlinTest)
                testImplementation(Dependencies.Kotlin.coroutinesTest)
                testImplementation(Dependencies.Testing.mockito)
            }
        }
    }
}
