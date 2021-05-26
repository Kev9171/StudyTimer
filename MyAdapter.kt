package com.mp.mp_time

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(var items:ArrayList<MyData>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, data: MyData, position: Int)
    }

    var icon: ArrayList<Int> = arrayListOf(R.drawable.ic_baseline_menu_book_24, R.drawable.ic_baseline_auto_graph_24, R.drawable.ic_baseline_library_music_24, R.drawable.ic_baseline_color_lens_24)


    var itemClickListener:OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val headline = itemView.findViewById<TextView>(R.id.headline)
        val sub = itemView.findViewById<TextView>(R.id.sub)
        val icon = itemView.findViewById<ImageView>(R.id.icon)
        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.headline.text = items[position].headline
        holder.sub.text = items[position].sub
        holder.icon.setImageResource(icon[position])

    }
}


