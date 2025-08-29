package com.lky.sunnyweather.logic.network

import com.lky.sunnyweather.SunnyWeatherApplication
import com.lky.sunnyweather.logic.model.DailyResponse
import com.lky.sunnyweather.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//使用了@Path注解来向请求接口中动态传入经 纬度的坐标。
//这两个方法的返回值分别被声明成了Call<RealtimeResponse>和 Call<DailyResponse>
interface WeatherService {

    //获取实时的天气信息
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<RealtimeResponse>

    //获取未来的天气信息
    @GET("v2.5/${SunnyWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String):
            Call<DailyResponse>
}