package com.mp.mp_time.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.databinding.RowDDayBinding
import com.mp.mp_time.databinding.RowScheduleBinding
import java.text.SimpleDateFormat
import java.util.*

class ScheduleAdapter(var items: MutableList<Schedule>): RecyclerView.Adapter<ScheduleAdapter.ViewHolder>() {
    var itemClickListener: OnItemClickListener? = null
    val ONE_DAY = 24 * 60 * 60 * 1000
    lateinit var date: Date
    var curPos: Int = 0
    var hasContent: Boolean = false

    interface OnItemClickListener {
        fun onDateClick(holder: ScheduleAdapter.ViewHolder, view: View, data: Schedule)
    }

    inner class ViewHolder(val binding: RowDDayBinding, val sbinding: RowScheduleBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                //클릭 이벤트 처리
                val selectedSchedule = items[position]
                curPos = position
                if(selectedSchedule.dDay){
                    val formatter = SimpleDateFormat("yyyy-M-dd")
                    date = formatter.parse(selectedSchedule.date)
                    hasContent = !selectedSchedule.content.isNullOrBlank()
                    itemClickListener?.onDateClick(this, it, items[adapterPosition])
                }
            }
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

    override fun getItemViewType(position: Int): Int {
        return position
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val ddayview = RowDDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val scheduleview = RowScheduleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(ddayview, scheduleview)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val date = items[position].date.split("-")
            if(items[position].dDay)
                testName.text = items[position].title
                testDate.text = getDday(date[0].toInt(), date[1].toInt(), date[2].toInt())
        }
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

    private fun getDday(dyear: Int, dmonth: Int, dday: Int): String? {
        // D-day 설정
        val ddayCalendar: Calendar = Calendar.getInstance()
        ddayCalendar.set(dyear, dmonth-1, dday)

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        val dday: Long = ddayCalendar.timeInMillis / ONE_DAY
        val today: Long = Calendar.getInstance().timeInMillis / ONE_DAY
        var result = dday - today

        // 출력 시 d-day 에 맞게 표시
        val strFormat: String
        if (result > 0) {
            strFormat = "D-%d"
        } else if (result == 0L) {
            strFormat = "D-Day"
        } else {
            result *= -1
            strFormat = "D+%d"
        }
        return String.format(strFormat, result)
    }
}