package com.mp.mp_time.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.RowSubjectBinding

class SubjectAdapter(var items: MutableList<Subject>):
RecyclerView.Adapter<SubjectAdapter.ViewHolder>(){
    var itemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onTimerClick(holder: ViewHolder, view: View, data: Subject, position: Int)
        fun onLongClick(holder: ViewHolder, view: View, data: Subject, position: Int)
    }

    inner class ViewHolder(val binding: RowSubjectBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                if(binding.subjectDetails.visibility == View.VISIBLE){
                    binding.subjectDetails.visibility = View.GONE
                } else {
                    binding.subjectDetails.visibility = View.VISIBLE
                }
            }
            binding.startBtn.setOnClickListener {
                itemClickListener?.onTimerClick(this, it, items[adapterPosition], adapterPosition)
            }
            binding.root.setOnLongClickListener {
                itemClickListener?.onLongClick(this@ViewHolder, it, items[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
        }
    }

    fun changeItems(newItems: MutableList<Subject>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun showDetails(viewHolder: ViewHolder, pos: Int) {
        if(viewHolder.binding.subjectDetails.visibility == View.VISIBLE){
            viewHolder.binding.subjectDetails.visibility = View.GONE
        } else {
            viewHolder.binding.subjectDetails.visibility = View.VISIBLE
        }
    }

    fun moveItem(oldPos: Int, newPos: Int) {
        val item = items[oldPos]
        items.removeAt(oldPos)
        items.add(newPos, item)
        notifyItemMoved(oldPos, newPos)
    }

    fun removeItem(pos: Int) {
        items.removeAt(pos)
        notifyItemRemoved(pos)
    }

    fun addItem(data: Subject) = items.add(data)
    fun addItems(newItems: MutableList<Subject>) {
        items = newItems
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowSubjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            subjectName.text = items[position].subName
            subjectTime.text = items[position].achievedTime
        }
    }

    override fun getItemCount(): Int {
        return items.size

    }
}