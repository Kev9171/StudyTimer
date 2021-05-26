package com.mp.mp_time

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalmAdapter(var items:ArrayList<MusicData>) : RecyclerView.Adapter<CalmAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, mdata: MusicData, position: Int)
    }

    var itemClickListener:OnItemClickListener?=null


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var title = itemView.findViewById<TextView>(R.id.titleText)
        var sub = itemView.findViewById<TextView>(R.id.subText)
        var state = itemView.findViewById<ImageView>(R.id.stateImage)

        init {
            itemView.setOnClickListener {
                itemClickListener?.OnItemClick(this,it,items[adapterPosition],adapterPosition)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.music_row,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = items[position].title
        holder.sub.text = items[position].sub
        if(items[position].flag == false){
            holder.state.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }else{
            holder.state.setImageResource(R.drawable.ic_baseline_pause_24)
        }

    }
}