plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.protobuf)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.qtglobal.practicaltest"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.qtglobal.practicaltest"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug") // Use testing
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

// protobuf configuration
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.24.4"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.hilt.android.v2571)
    testImplementation(libs.junit.junit)
    testImplementation(libs.junit.jupiter)
    ksp(libs.hilt.android.compiler)

    // protobuf dependencies
    implementation(libs.protobuf.kotlin.lite)

    // Room dependencies
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // SQLCipher dependency
//    implementation(libs.android.database.sqlcipher.v450)
//    implementation("net.zetetic:sqlcipher-android:4.12.0")


//    implementation("androidx.security:security-crypto:1.1.0-alpha06")


    // EncryptedSharedPreferences for secure key storage
//    implementation(libs.androidx.security.crypto)


    // SQLCipher for Android (Room openHelperFactory)
    implementation("net.zetetic:sqlcipher-android:4.12.0")

    // EncryptedSharedPreferences for secure key storage
    implementation(libs.androidx.security.crypto.v110alpha06)



    testImplementation(libs.junit)
    // Unit testing dependencies
    testImplementation("io.mockk:mockk:1.13.8")
//    testImplementation("app.cash.turbine:turbine:1.0.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("com.google.truth:truth:1.1.4")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}