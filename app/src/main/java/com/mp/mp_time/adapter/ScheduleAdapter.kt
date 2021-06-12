package com.mp.mp_time.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.databinding.RowScheduleBinding
import java.util.*

class ScheduleAdapter(var items: MutableList<Schedule>): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null
    lateinit var date: Date
    var curPos: Int = 0
    var hasContent: Boolean = false

    interface OnItemClickListener {
        fun onDateClick(holder: ScheduleAdapter.ViewHolder, view: View, data: Schedule)
    }

    inner class ViewHolder(val sbinding: RowScheduleBinding) : RecyclerView.ViewHolder(sbinding.root) {
        init {
            sbinding.root.setOnClickListener {
                if(sbinding.scheduleContent.visibility == View.GONE){
                    sbinding.scheduleContent.visibility = View.VISIBLE
                }
                else{
                    sbinding.scheduleContent.visibility = View.GONE
                }
            }
        }
    }

    fun addItem(schedule: Schedule) {
        items.add(schedule)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val scheduleview = RowScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(scheduleview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.sbinding.apply {
            if(!items[position].content.isNullOrBlank()){
                scheduleTitle.text = items[position].title
                scheduleDate.text = items[position].date
                scheduleContent.text = items[position].content
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}