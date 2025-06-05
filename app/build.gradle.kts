import java.io.FileInputStream
import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
}


val secretsPropertiesFile = rootProject.file("secrets.properties")
val secretsProperties = Properties()
if (secretsPropertiesFile.exists()) {
    secretsProperties.load(FileInputStream(secretsPropertiesFile))
}
android {
    namespace = "com.example.schumannechoes"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.schumannechoes"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField(type = "String", name = "maps_api_key", value = "\"${secretsProperties["MAPS_API_KEY"]}\"")

        // Add to resources (for XML access)
        resValue(type = "string", name = "maps_api_key", value = "\"${secretsProperties["MAPS_API_KEY"]}\"")
    }
    buildFeatures {
        buildConfig=true // Enable BuildConfig fields

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.libraries.places:places:3.3.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    implementation("com.google.android.gms:play-services-location:19.0.1")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    implementation("com.airbnb.android:lottie:6.4.0")

}