import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.get

class CommonUiModulePlugin : CommonModulePlugin() {

    override fun apply(project: Project) {
        super.apply(project)

        with(project.plugins) {
            apply("androidx.navigation.safeargs.kotlin")
        }

        with(project.extensions["android"] as BaseExtension) {
            with(buildFeatures) {
                viewBinding = true
                compose = true
            }

            @Suppress("UnstableApiUsage")
            composeOptions {
                kotlinCompilerExtensionVersion = Versions.COMPOSE
            }
        }

        project.dependencies {
            addCompose()
        }
    }
}
