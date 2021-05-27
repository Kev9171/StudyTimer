package com.mp.mp_time.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R
import com.mp.mp_time.adapter.MyAdapter
import com.mp.mp_time.data.MyData
import java.util.*
import kotlin.collections.ArrayList


class UserSettingsFragment : Fragment() {
    var data: ArrayList<MyData> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyAdapter

    var icon: ArrayList<Int> = arrayListOf(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_auto_graph_24, R.drawable.ic_baseline_library_music_24, R.drawable.ic_baseline_color_lens_24)



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_settings, container, false)

        initData()
        initRecyclerView(view)

        return view
    }

    private fun initRecyclerView(view:View) {
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter = MyAdapter(data)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager(activity).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


        adapter.itemClickListener = object :  MyAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, data: MyData, position: Int) {
                val intent = Intent(activity, SetThemeActivity::class.java)
                startActivity(intent)

            }

        }
        recyclerView.adapter=adapter
    }

    private fun initData() {
        val scan = Scanner(resources.openRawResource(R.raw.menus))
        var i:Int = 0
        while(scan.hasNextLine()){
            val head = scan.nextLine()
            val sub = scan.nextLine()
            data.add(MyData(icon[i],head,sub))
            i++
        }
        scan.close()
    }
}