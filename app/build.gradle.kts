plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
}

android {
    namespace = "br.com.sayurienterprise.photostore"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.sayurienterprise.photostore"
        minSdk = 25
        targetSdk = 34
        versionCode = 5
        versionName = "3.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("key1") {
            storeFile = file("\\keystore\\releasekey.jks")
            storePassword = "android!@#"
            keyAlias = "volvo-release"
            keyPassword = "android!@#"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("key1")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    applicationVariants.all {
        outputs.all {
            val buildType = buildType.name
            val versionName = versionName

            (this as com.android.build.gradle.internal.api.BaseVariantOutputImpl).outputFileName =
                "PhotoStore_v${versionName}_${buildType}.apk"
        }
    }

}

dependencies {
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.21")
    implementation("androidx.room:room-runtime:2.4.0") // Room runtime
    implementation("androidx.room:room-ktx:2.4.0")  // Room KTX for coroutine support
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}