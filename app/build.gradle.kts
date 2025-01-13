plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.login"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.login"
        minSdk = 27
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }
    packagingOptions {
        exclude ("META-INF/LICENSE.md") // Loại bỏ tệp LICENSE.md
        exclude ("META-INF/LICENSE.txt") // Loại bỏ LICENSE.txt nếu có
        exclude ("META-INF/NOTICE.md") // Loại bỏ NOTICE.md nếu có
        exclude ("META-INF/NOTICE.txt") // Loại bỏ NOTICE.txt nếu có
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    viewBinding {
        enable = true
    }
    buildFeatures{
        dataBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.databinding.runtime)
    implementation(libs.androidx.databinding.compiler.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Lấy vị trí hiện tại của người dùng
    implementation ("com.google.android.gms:play-services-location:19.0.1")

    //Coroutine
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")

    //Kotlin flow
    implementation ("org.jetbrains.kotlinx:kotlinx-flow:1.6.0")

    //ViewModelScope
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Glide để tải ảnh thời tiết
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    //Googleplay Service
    implementation ("com.google.android.gms:play-services-location:18.0.0")

    //Koin
    implementation("io.insert-koin:koin-android:3.3.0")
    implementation("io.insert-koin:koin-core:3.3.0")
    implementation("io.insert-koin:koin-androidx-viewmodel:3.3.0")
}