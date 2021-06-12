package com.mp.mp_time.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.adapter.ScheduleAdapter
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.database.ScheduleDBHelper
import com.mp.mp_time.databinding.FragmentCalendarBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.util.*


class CalendarFragment : Fragment() {
    var binding: FragmentCalendarBinding? = null
    lateinit var scheduleDBHelper: ScheduleDBHelper
    val scheduleList = mutableListOf<Schedule>()
    val viewModel: StudyViewModel by activityViewModels()
    lateinit var adapter: ScheduleAdapter

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding!!.apply {
            DdayRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
            ScheduleRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = ScheduleAdapter(viewModel.scheduleList)
            val formatter = SimpleDateFormat("yyyy-M-dd")
            val date = Date(System.currentTimeMillis())
            var curSchedule = viewModel.getScheduleByDate(formatter.format(date).toString())
            //Toast.makeText(binding!!.root.context, formatter.format(date).toString(), Toast.LENGTH_SHORT).show()
            if(!curSchedule.isNullOrEmpty()){
                ScheduleRecyclerView.visibility = View.VISIBLE
                adapter.items.filter {schedule: Schedule -> schedule.equals(curSchedule)}
                adapter.notifyDataSetChanged()
//                adapter.scheduleDate.text = curSchedule[0].date
//                scheduleTitle.text = curSchedule[0].title
//                scheduleContent.text = curSchedule[0].content
                swipeLayout.isSwipeEnabled=true
            }

            addScheduleBtn.setOnClickListener {
                if (swipeLayout.visibility == View.VISIBLE) {
                    swipeLayout.visibility = View.GONE
                    addScheduleBtn.visibility = View.GONE
                    addBtn.visibility = View.VISIBLE
                    cancelBtn.visibility = View.VISIBLE
                    dDayCheck.visibility = View.VISIBLE
                    addSchedule.visibility = View.VISIBLE
                } else {
                    addBtn.visibility = View.GONE
                    cancelBtn.visibility = View.GONE
                    dDayCheck.visibility = View.GONE
                    addSchedule.visibility = View.GONE
                    addScheduleBtn.visibility = View.VISIBLE
                    swipeLayout.visibility = View.VISIBLE
                }
            }
            cancelBtn.setOnClickListener {
                nameEdit.text.clear()
                scheduleEdit.text.clear()
                addBtn.visibility = View.GONE
                cancelBtn.visibility = View.GONE
                dDayCheck.visibility = View.GONE
                addSchedule.visibility = View.GONE
                addScheduleBtn.visibility = View.VISIBLE
                swipeLayout.visibility = View.VISIBLE
                //swipeLayout.open(swipeLayout.dragEdge)
            }

            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

                val curDate = year.toString().plus("-") + (month + 1).toString()
                    .plus("-") + dayOfMonth.toString()
                curSchedule = viewModel.getScheduleByDate(curDate)
                if (!curSchedule.isNullOrEmpty()) {
                    ScheduleRecyclerView.visibility = View.VISIBLE
                    adapter.items.filter {schedule: Schedule -> schedule.equals(curSchedule)}
                    adapter.notifyDataSetChanged()
//                    scheduleDate.text = curSchedule!![0].date
//                    scheduleTitle.text = curSchedule!![0].title
//                    scheduleContent.text = curSchedule!![0].content
                    swipeLayout.isSwipeEnabled = true
                    swipeLayout.open(swipeLayout.dragEdge)
                } else {
//                    scheduleDate.text = ""
//                    scheduleTitle.text = ""
//                    scheduleContent.text = ""
                    ScheduleRecyclerView.visibility = View.GONE
                    swipeLayout.isSwipeEnabled = false
                }

                addBtn.setOnClickListener {
                    if (nameEdit.text.toString() == "")
                        Toast.makeText(requireContext(), "과목명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    else {
                        var dDay = dDayCheck.isChecked
                        viewModel.insertSchedule(
                            Schedule(
                                date = curDate,
                                title = nameEdit.text.toString(),
                                content = scheduleEdit.text.toString(),
                                dDay = dDay
                            )
                        )
                        nameEdit.text.clear()
                        scheduleEdit.text.clear()

                        addBtn.visibility = View.GONE
                        cancelBtn.visibility = View.GONE
                        dDayCheck.visibility = View.GONE
                        addSchedule.visibility = View.GONE
                        addScheduleBtn.visibility = View.VISIBLE
                        swipeLayout.visibility = View.VISIBLE
                        Toast.makeText(binding!!.root.context, "일정을 추가했습니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            }

            val simpleCallBack = object: ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.RIGHT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    adapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                    return true
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    val date = viewModel.scheduleList[viewHolder.adapterPosition].date
                    val title = viewModel.scheduleList[viewHolder.adapterPosition].title
                    viewModel.deleteScheduleByDateAndTitle(date, title)
                    adapter.removeItem(viewHolder.adapterPosition)
                    Toast.makeText(binding!!.root.context, "일정을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallBack)
            itemTouchHelper.attachToRecyclerView(DdayRecyclerView)

            adapter.itemClickListener = object : ScheduleAdapter.OnItemClickListener {
                override fun onDateClick(holder: ScheduleAdapter.ViewHolder, view: View, data: Schedule) {
                    calendarView.date = adapter.date.time

                    if(adapter.hasContent) {
//                        scheduleDate.text = viewModel.scheduleList[adapter.curPos].date
//                        scheduleTitle.text = viewModel.scheduleList[adapter.curPos].title
//                        scheduleContent.text = viewModel.scheduleList[adapter.curPos].content
                        swipeLayout.isSwipeEnabled = true
                        adapter.items.filter {schedule: Schedule -> schedule.equals(viewModel.scheduleList[adapter.curPos])}
                        adapter.notifyDataSetChanged()
                        swipeLayout.open(swipeLayout.dragEdge)
                    }
                }
            }
            DdayRecyclerView.adapter = adapter
        }
    }

}