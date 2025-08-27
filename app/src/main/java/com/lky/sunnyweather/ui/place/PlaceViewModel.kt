package com.lky.sunnyweather.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.lky.sunnyweather.data.Repository
import com.lky.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {

    /*
类型	                            对外是否可变	是否可观察	所属包	            用途
ArrayList<Place>	            ✅ 可变	    ❌ 不可观察	kotlin.collections	普通数据容器
LiveData<ArrayList<Place>>	    ❌ 不可变	✅ 可观察	androidx.lifecycle	观察数据变化
MutableLiveData<ArrayList<Place>>✅ 可变	✅ 可观察	androidx.lifecycle	修改 + 观察数据
     */

    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    //当源 LiveData 发生变化时，转为另一个 LiveData，并自动观察最新的，同时取消对旧 LiveData 的观察。
    /*
    输入 "北" → 开始观察 searchPlaces("北")
    输入 "北京" → 停止观察 searchPlaces("北")，切换到观察 searchPlaces("北京")
    输入 "北京市" → 停止观察 searchPlaces("北京")，切换到观察 searchPlaces("北京市")
    ✅ 最终：只接收 searchPlaces("北京市") 的结果，前面的请求结果被自动丢弃。
    防止出现
    更新网络请求 "北京市" 可能比 "北" 返回得快
    UI 先显示 "北京市"的结果，然后被 "北" 覆盖，再被 "北京" 覆盖
     */
    val placeLiveData = searchLiveData.switchMap { query ->
        Repository.searchPlaces(query)
    }

    fun searchPlaces(query: String) {
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}