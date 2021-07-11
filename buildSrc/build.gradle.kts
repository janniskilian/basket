plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("common-module-plugin") {
            id = "common-module-plugin"
            implementationClass = "CommonModulePlugin"
        }
        register("common-ui-module-plugin") {
            id = "common-ui-module-plugin"
            implementationClass = "CommonUiModulePlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    compileOnly(gradleApi())

    implementation(kotlin("gradle-plugin", "1.5.10"))
    implementation("com.android.tools.build:gradle:7.1.0-alpha03")
}
