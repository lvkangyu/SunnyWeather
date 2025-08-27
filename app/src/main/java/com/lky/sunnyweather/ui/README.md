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