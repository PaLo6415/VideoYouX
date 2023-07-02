import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

fun buildInfo(type: String): Any? {
    when (type) {
        "name" -> {
            return "VideoYouX"
        }

        "isCanary" -> {
            return "true"
        }

        "version" -> {
            return "0.9.9"
        }

        "subVersion" -> {
            return "Canary04"
        }

        else -> {
            return null
        }
    }
}

android {
    namespace = "com.clearpole.videoyoux"
    compileSdk = 34
    defaultConfig {
        applicationId = namespace
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = if (buildInfo("isCanary") == "true") {
            buildInfo("version").toString() + " - " + buildInfo("subVersion").toString()
        } else {
            buildInfo("version").toString()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters += "arm64-v8a"
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val properties = Properties()
    runCatching { properties.load(project.rootProject.file("local.properties").inputStream()) }
    val keystorePath = properties.getProperty("KEYSTORE_PATH") ?: System.getenv("KEYSTORE_PATH")
    val keystorePwd = properties.getProperty("KEYSTORE_PASS") ?: System.getenv("KEYSTORE_PASS")
    val alias = properties.getProperty("KEY_ALIAS") ?: System.getenv("KEY_ALIAS")
    val pwd = properties.getProperty("KEY_PASSWORD") ?: System.getenv("KEY_PASSWORD")
    if (keystorePath != null) {
        signingConfigs {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = keystorePwd
                keyAlias = alias
                keyPassword = pwd
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        release {
//                isMinifyEnabled = true
//                isShrinkResources = true
//                proguardFiles(
//                    getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
//                )
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }

    kotlinOptions {
        jvmTarget = "19"
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildToolsVersion = "34.0.0"
    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                val code = (0..99999).random()
                this.outputFileName = if (buildInfo("isCanary") == "false") {
                    buildInfo("name").toString() + buildInfo("version") + "-$code-${buildType.name}.apk"
                } else {
                    buildInfo("name").toString() + "-" + buildInfo("version") + "-" + buildInfo("subVersion").toString() + "-$code-${buildType.name}.apk"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(platform(libs.compose.bom))
    implementation(files("libs/json.jar"))
    implementation(libs.androidx.viewbinding)
    implementation(platform(libs.compose.bom))
    implementation(platform(libs.compose.bom))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.palette)
    implementation(libs.monet.compat)
    implementation(libs.immersionbar)
    implementation(libs.utilcodex)
    implementation(libs.serialize)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.xx.permissions)
    implementation(libs.tooltip)
    implementation(libs.brv)
    implementation(libs.glide)
    implementation(libs.gsyVideoPlayer.java)
    implementation(libs.gsyVideoPlayer.arm64)
    implementation(libs.androidx.ui.viewbinding)
}