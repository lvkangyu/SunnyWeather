package com.lky.sunnyweather.logic.network

import com.lky.sunnyweather.SunnyWeatherApplication
import com.lky.sunnyweather.logic.model.PlaceResponse


//对其内部的okhttp3.Call实例的包装和增强，自带Gson将响应体解析为对象
import retrofit2.Call
//表示一个已经准备好可以执行的网络请求
//import okhttp3.Call   同步执行execute()异步执行enqueue()


import retrofit2.http.GET
//用于在 HTTP 请求的 URL 中添加查询参数
import retrofit2.http.Query
//用于在 DAO 接口中定义自定义的 SQL 查询语句。
//androidx.room.Query

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}