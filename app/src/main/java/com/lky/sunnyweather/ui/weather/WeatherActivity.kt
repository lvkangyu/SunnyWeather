package com.lky.sunnyweather.ui.weather

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.lky.sunnyweather.R
import com.lky.sunnyweather.logic.model.Weather
import com.lky.sunnyweather.logic.model.getSky
import com.lky.sunnyweather.ui.place.PlaceActivity
import java.text.SimpleDateFormat
import java.util.Locale


class WeatherActivity : AppCompatActivity() {
    //延迟初始化 (Lazy Initialization):
    // 1️⃣ViewModel在第一次被访问时才创建2️⃣默认保证在其作用域内被访问时只初始化一次
    //仍能使用的java风格
    //val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    //已弃用的java风格
    //val viewModel by lazy { ViewModelProviders.of(this).get(WeatherViewModel::class.java) }
    //kotlin最佳实践
    val viewModel: WeatherViewModel by viewModels()
    /*
    // 在Activity或Fragment中（如果viewModelStoreOwner是自身）
    private val viewModel: PlaceViewModel by viewModels()
    // 如果需要自定义ViewModelFactory
    private val viewModel: PlaceViewModel by viewModels {
        // 返回你的自定义Factory
        MyViewModelFactory(...)
    }
    // 在Fragment中，想要共享Activity级别的ViewModel
    private val sharedViewModel: PlaceViewModel by activityViewModels()
     */

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var swipeRefresh: SwipeRefreshLayout
    //now.xml中
    private lateinit var placeName: TextView
    private lateinit var currentTemp: TextView
    private lateinit var currentSky: TextView
    private lateinit var currentAQI: TextView
    private lateinit var nowLayout: RelativeLayout
    //forecast.xml中
    private lateinit var forecastLayout: LinearLayout
    //life_index.xml中
    private lateinit var coldRiskText: TextView
    private lateinit var dressingText: TextView
    private lateinit var ultravioletText: TextView
    private lateinit var carWashingText: TextView
    private lateinit var weatherLayout: ScrollView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.statusBarColor = Color.TRANSPARENT
        }
        setContentView(R.layout.activity_weather)

        swipeRefresh = findViewById(R.id.swipeRefresh)
        drawerLayout = findViewById(R.id.drawerLayout)
        //now.xml中
        placeName = findViewById(R.id.placeName)
        currentTemp = findViewById(R.id.currentTemp)
        currentSky = findViewById(R.id.currentSky)
        currentAQI = findViewById(R.id.currentAQI)
        nowLayout = findViewById(R.id.nowLayout)
        //forecast.xml中
        forecastLayout = findViewById(R.id.forecastLayout)
        //life_index.xml中
        coldRiskText = findViewById(R.id.coldRiskText)
        dressingText = findViewById(R.id.dressingText)
        ultravioletText = findViewById(R.id.ultravioletText)
        carWashingText = findViewById(R.id.carWashingText)
        weatherLayout = findViewById(R.id.weatherLayout)

        if (viewModel.locationLng.isEmpty()) {
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            swipeRefresh.isRefreshing = false
        })
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        //在切换城市按钮的点击事件中调用DrawerLayout的 openDrawer()方法来打开滑动菜单
        findViewById<Button>(R.id.navBtn).setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        //TODO 写到 ViewModel 中
        //val lottie = findViewById<LottieAnimationView>(R.id.clearBtn)
        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            Toast.makeText(this, "清空SharedPreferences", Toast.LENGTH_SHORT).show()
            viewModel.clearPlace()
            startActivity(Intent(this, PlaceActivity::class.java))
//            // 获取 SharedPreferences 实例
//            val sharedPreferences = getSharedPreferences("your_preferences_name", MODE_PRIVATE)
//            // 或者使用默认的 SharedPreferences
//            // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//            val editor = sharedPreferences.edit()
//            // 清空所有数据
//            editor.clear()
//            // 提交更改 (异步)
//            editor.apply()
        }
        //监听DrawerLayout的状态，当滑动菜单被隐藏 的时候，同时也要隐藏输入法
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {}

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {}

            override fun onDrawerOpened(drawerView: View) {}

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(drawerView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        })
    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
        // 填充now.xml布局中数据
        val currentTempText = "${realtime.temperature.toInt()} ℃"
        currentTemp.text = currentTempText
        currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
        currentAQI.text = currentPM25Text
        nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        // 填充forecast.xml布局中的数据
        forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false)
            val dateInfo: TextView = view.findViewById(R.id.dateInfo)
            val skyIcon: ImageView = view.findViewById(R.id.skyIcon)
            val skyInfo: TextView = view.findViewById(R.id.skyInfo)
            val temperatureInfo: TextView = view.findViewById(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        coldRiskText.text = lifeIndex.coldRisk[0].desc
        dressingText.text = lifeIndex.dressing[0].desc
        ultravioletText.text = lifeIndex.ultraviolet[0].desc
        carWashingText.text = lifeIndex.carWashing[0].desc
        weatherLayout.visibility = View.VISIBLE
    }

}