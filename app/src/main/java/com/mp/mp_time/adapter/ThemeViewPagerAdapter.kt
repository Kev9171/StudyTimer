package com.mp.mp_time.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R

class ThemeViewPagerAdapter(themeList: ArrayList<Int>) : RecyclerView.Adapter<ThemeViewPagerAdapter.PagerViewHolder>() {
    var item = themeList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = item.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.theme.setImageResource(item[position])

    }

    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.theme_list_item, parent, false)){

        val theme = itemView.findViewById<ImageView>(R.id.themeView)
        //val themeText = itemView.findViewById<TextView>(R.id.themeText)
    }
}