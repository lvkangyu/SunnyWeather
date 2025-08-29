# SunnyWeather
1.PlaceActivity有一个带自定义导航栏、搜索框、RecyclerView的Fragment
2.PlaceFragment中
修改onActivityCreated为onViewCreated
修改this为viewLifecycleOwner
修改ViewBinding为findViewById

给编辑框设置文本变化监听器
如果为空 → 隐藏循环视图，显示背景图，并清空数据，刷新循环视图。
如果有内容 → 调用 viewModel.searchPlaces(content) 发起搜索。

观察 viewModel.placeLiveData
更新ui为响应的place列表，为空则“未能查询到任何地点”

原来的系统架构
![替代文本](/Users/zjs/Desktop/原架构.png)
原来的查询失败
![替代文本](/Users/zjs/Desktop/原查询失败.png)

3.WeatherActivity是一个抽屉视图，
主视图是下拉刷新视图+滚动视图，导入了三个xml文件，当日天气带对应图片，预报，生活指数
第二个子视图从相对于从左向右的文字的左边滑出，带PlaceFragment

app
├── ui
│   └── views                # 存放Activity和Fragment等视图层代码
│       ├── MainActivity.kt
│       └── DetailFragment.kt
│
├── viewmodel                # 存放ViewModel相关代码
│   ├── MainViewModel.kt
│   └── DetailViewModel.kt
│
├── data
│   ├── repository           # 数据仓库，处理数据逻辑
│   │   ├── MainRepository.kt
│   │   └── DetailRepository.kt
│   ├── network              # 网络请求相关的代码
│   │   ├── ApiService.kt    # 使用Retrofit定义的API服务接口
│   │   ├── ApiHelper.kt     # 网络请求的帮助类
│   │   └── RetrofitBuilder.kt# Retrofit实例构建器
│   └── local                # 本地数据库相关的代码（如Room数据库）
│       ├── AppDatabase.kt
│       └── DatabaseHelper.kt