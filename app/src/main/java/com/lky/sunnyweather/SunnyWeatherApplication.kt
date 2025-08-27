package com.lky.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/*
    在AndroidManifest的<application>下android:name=".SunnyWeatherApplication"
    系统会自动创建这个类的实例，并调用它的 onCreate() 方法。
    任何地方都可以通过 SunnyWeatherApplication.context获取到上下文
 */
class SunnyWeatherApplication : Application() {
    companion object {

        //彩云科技开放平台的令牌
        const val TOKEN = "rPn9apDsdTzElcrM"

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}