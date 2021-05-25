package com.mp.mp_time.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.data.Test
import com.mp.mp_time.databinding.RowDDayBinding
import com.mp.mp_time.view.CalendarFragment
import com.mp.mp_time.view.MainActivity
import java.util.*

class TestAdapter(var items: MutableList<Test>):RecyclerView.Adapter<TestAdapter.ViewHolder>(){
    var itemClickListener: OnItemClickListener? = null
    val ONE_DAY = 24 * 60 * 60 * 1000

    interface OnItemClickListener {
        fun onDateClick(holder: TestAdapter.ViewHolder, view: View, data: Test)
    }

    inner class ViewHolder(val binding: RowDDayBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                //클릭 이벤트 처리
                val builder = AlertDialog.Builder(binding.root.context)
                        .setTitle("삭제 확인")
                        .setMessage(binding.testName.text.toString() + "을 삭제하시겠습니까?")
                        //.setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton("OK") {
                            _, _ -> removeItem(adapterPosition)
                            Toast.makeText(binding.root.context, "삭제 완료했습니다.", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("NO") {
                            _, _ ->
                            Toast.makeText(binding.root.context, "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                        .show();
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

    fun addItem(data: Test) = items.add(data)
    fun addItems(newItems: MutableList<Test>) {
        items = newItems
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RowDDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            testName.text = items[position].name
            testDate.text = getDday(items[position].dyear,
                    items[position].dmonth,
                    items[position].dday)
        }
    }

    override fun getItemCount(): Int {
        return items.size

    }

    private fun getDday(dyear: Int, dmonth: Int, dday: Int): String? {
        // D-day 설정
        val ddayCalendar: Calendar = Calendar.getInstance()
        ddayCalendar.set(dyear, dmonth, dday)

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        val dday: Long = ddayCalendar.getTimeInMillis() / ONE_DAY
        val today: Long = Calendar.getInstance().getTimeInMillis() / ONE_DAY
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