plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    // Dependencias principales
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.databinding:databinding-runtime:7.0.4")
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // Dependencia estándar de Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")

    // Dependencias de Room
    implementation("androidx.room:room-runtime:2.5.0")
    implementation(libs.androidx.activity)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx) // Reemplaza con la versión más reciente si es necesario
    kapt("androidx.room:room-compiler:2.5.0") // Procesador de anotaciones (KAPT para Kotlin)

    implementation("androidx.room:room-ktx:2.5.0") // Soporte para corutinas (opcional, pero recomendado)

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.room:room-ktx:2.5.0")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    // Dependencias de pruebas
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.picasso:picasso:2.8")
    implementation ("com.google.firebase:firebase-auth:22.1.0")
    implementation ("com.google.android.gms:play-services-auth:20.6.0")
    implementation ("com.google.firebase:firebase-firestore:24.9.0")

    //Dependencias de CameraX
    implementation ("androidx.camera:camera-core:1.3.0")
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-video:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("androidx.camera:camera-extensions:1.3.0")
    implementation ("com.google.guava:guava:31.0.1-android")

    //Dependencias de exoplayer
    implementation("androidx.media3:media3-exoplayer:1.5.0")
    implementation("androidx.media3:media3-exoplayer-dash:1.5.0")
    implementation("androidx.media3:media3-ui:1.5.0")

}
