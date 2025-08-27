package com.lky.sunnyweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lky.sunnyweather.R

class PlaceFragment : Fragment() {

    //使用 lazy 延迟初始化 ViewModel
    //ViewModel 的生命周期与 Fragment 绑定，(View 重建也不会丢失数据)
    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter: PlaceAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchPlaceEdit: EditText
    private lateinit var bgImageView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_place, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(activity)

        searchPlaceEdit = view.findViewById(R.id.searchPlaceEdit)
        bgImageView = view.findViewById(R.id.bgImageView)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(this, viewModel.placeList)
        recyclerView.adapter = adapter

        //监听搜索框输入
        searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        //观察 viewModel.placeLiveData
        /*
            Fragment本身 和 Fragment的View 拥有 两个独立但相关的生命周期。
            生命周期拥有者为 viewLifecycleOwner 和 this(fragment)
            view被销毁重建，但Fragment 实例没有被销毁
            当onDestroyView() 执行时，Observer 会自动移除
        */
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer { result ->
            //获取查询到的place列表
            val places = result.getOrNull()
            if (places != null) {
                recyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT)
                    .show()
                result.exceptionOrNull()?.printStackTrace()
                recyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
            }
        })
    }
}
/*
// 1. 初始进入（竖屏）
MainActivity: onCreate()
    → onStart()
    → onResume()

Fragment: onAttach()
    → onCreate()
    → onCreateView()           // ✅ View 创建
    → onViewCreated()
    → onViewStateRestored()
    → onStart()
    → onResume()

// 此时 UI 显示正常

// 2. 用户旋转屏幕 → 系统配置变更（Configuration Change）
// 系统会销毁并重建 Activity 及其包含的 Fragment 的 View

Fragment: onPause()
    → onStop()

Fragment: onDestroyView()   // ✅ View 被销毁！但 Fragment 实例还在
    // ❗此时：recyclerView, bgImageView 等 View 引用变为 null

Fragment: onCreate()        // ❌ Fragment 实例未被销毁，不会走 onCreate
    // （如果 Fragment 没有 setRetainInstance，这里会走 onCreate）

// 系统重建 View
Fragment: onCreateView()    // ✅ View 重新创建
    → onViewCreated()            // ✅ 重新绑定 View
    → onViewStateRestored()
    → onStart()
    → onResume()

MainActivity: onRestart()
    → onStart()
    → onResume()
📌 关键点
onDestroyView() 被调用 → View 被销毁
onCreate() 没有被调用 → Fragment 实例仍然存活
onCreateView() 再次被调用 → View 被重新创建
✅ 结论：View 被销毁并重建了，但 Fragment 实例没有被销毁。
 */