import extension.setupDependencyInjection
import extension.testCommonDependencies

plugins {
    id("io.element.android-compose-library")
    id("kotlin-parcelize")
}

android {
    namespace = "io.element.android.features.bot.impl"
}

setupDependencyInjection()

dependencies {
    api(projects.features.bot.api)
    implementation(projects.libraries.core)
    implementation(projects.libraries.architecture)
    implementation(projects.libraries.matrix.api)
    implementation(projects.libraries.designsystem)
    implementation(projects.libraries.uiStrings)
    implementation(projects.services.toolbox.api)
    
    implementation(libs.coroutines.core)
    implementation(libs.timber)
    
    testCommonDependencies(libs)
    testImplementation(projects.libraries.matrix.test)
}
