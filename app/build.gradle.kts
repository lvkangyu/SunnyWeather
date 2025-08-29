plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    //ksp 第 1 步 将其作为插件添加到您的模块 build.gradle.kts 并同步
    id("com.google.devtools.ksp") version "2.2.0-2.0.2"
    //id("com.google.dagger.hilt.android") version "2.57.1"
}

android {
    namespace = "com.lky.sunnyweather"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.lky.sunnyweather"
        minSdk = 30
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //扩展函数activityViewModels()
    implementation(libs.fragment)
    //下拉刷新布局
    implementation(libs.androidx.swiperefreshlayout)
    //更强大的滚动控件
    implementation(libs.androidx.recyclerview)
    //动画
    implementation(libs.lottie)
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    //引入okhttp、okio
    implementation(libs.okhttp)
    //协程
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    //开源项目：图片圆形化
    implementation(libs.circleimageview)

    //lifecycle
    implementation(libs.viewmodel)
    implementation(libs.livedata)
    implementation(libs.runtime)//提供 LifecycleOwner、LifecycleService、Kotlin 扩展等核心功能
    //Room
    //在上面plugins中alias(libs.plugins.kotlin.kapt)
    implementation(libs.room.runtime)
    //ksp 第 2 步 添加到您的实现
    ksp(libs.room.compiler)

    // WorkManager
    implementation(libs.work.runtime)

    //    // Compose
//    implementation("androidx.activity:activity-compose:1.8.0")
//    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
//    implementation("androidx.compose.ui:ui")
//    implementation("androidx.compose.ui:ui-graphics")
//    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
//    implementation("androidx.navigation:navigation-compose:2.7.4")


}