package com.mp.mp_time.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.data.Place
import com.mp.mp_time.databinding.RowPlaceBinding

class MyPlaceAdapter (var items: MutableList<Place>):
    RecyclerView.Adapter<MyPlaceAdapter.ViewHolder>(){

    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(holder: ViewHolder, view: View, data: Place, position: Int)
    }

    inner class ViewHolder(val binding: RowPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(this, it, items[adapterPosition], adapterPosition)
            }
        }
    }

    fun addItem(data: Place) = items.add(data)

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            placeName.text = items[position].placeName
            ratingBar.rating = items[position].rating
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}