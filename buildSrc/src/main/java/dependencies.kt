@file:Suppress("ClassName", "unused")

object Build {
    const val applicationId = "com.ivianuu.listprefs.sample"
    const val buildToolsVersion = "28.0.3"

    const val compileSdk = 28
    const val minSdk = 17
    const val targetSdk = 28
    const val versionCode = 1
    const val versionName = "0.0.1"
}

object Publishing {
    const val groupId = "com.ivianuu.listprefs"
    const val vcsUrl = "https://github.com/IVIanuu/list-prefs"
    const val version = "${Build.versionName}-dev-1"
}

object Versions {
    const val androidGradlePlugin = "3.3.0"
    const val androidxAppCompat = "1.0.2"
    const val bintray = "1.8.4"
    const val constraintLayout = "1.1.3"
    const val kotlin = "1.3.21"
    const val list = "0.0.1-dev-13"
    const val materialComponents = "1.1.0-alpha05"
    const val materialDialogs = "2.0.3"
    const val mavenGradle = "2.1"
}

object Deps {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidGradlePlugin}"

    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompat}"

    const val bintrayGradlePlugin =
        "com.jfrog.bintray.gradle:gradle-bintray-plugin:${Versions.bintray}"

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    const val list = "com.ivianuu.list:list:${Versions.list}"
    const val listAnnotations = "com.ivianuu.list:list-annotations:${Versions.list}"
    const val listCommon = "com.ivianuu.list:list-common:${Versions.list}"
    const val listCompiler = "com.ivianuu.list:list-compiler:${Versions.list}"

    const val materialComponents =
        "com.google.android.material:material:${Versions.materialComponents}"

    const val materialDialogs = "com.afollestad.material-dialogs:core:${Versions.materialDialogs}"
    const val materialDialogsInput =
        "com.afollestad.material-dialogs:input:${Versions.materialDialogs}"

    const val mavenGradlePlugin = "com.github.dcendents:android-maven-gradle-plugin:${Versions.mavenGradle}"
}