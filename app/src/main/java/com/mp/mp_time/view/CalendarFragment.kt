package com.mp.mp_time.view

import android.os.Bundle
import android.util.Log
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
import com.mp.mp_time.adapter.DdayAdapter
import com.mp.mp_time.adapter.ScheduleAdapter
import com.mp.mp_time.data.Schedule
import com.mp.mp_time.database.ScheduleDBHelper
import com.mp.mp_time.databinding.FragmentCalendarBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class CalendarFragment : Fragment() {
    var binding: FragmentCalendarBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    lateinit var scheduleAdapter: ScheduleAdapter
    lateinit var dDayAdapter: DdayAdapter
    var curDate = ""

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
            swipeLayout.isSwipeEnabled = true
            swipeLayout.open(swipeLayout.dragEdge)
            curDate = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식

            // scheduleAdapter: 일정
            scheduleAdapter = ScheduleAdapter(mutableListOf())
            ScheduleRecyclerView.adapter = scheduleAdapter
            val curSchedule = viewModel.scheduleList.filter {
                it.date == curDate
            } as MutableList<Schedule>
            //Toast.makeText(binding!!.root.context, formatter.format(date).toString(), Toast.LENGTH_SHORT).show() // ddd

            if (!curSchedule.isNullOrEmpty()) {
                scheduleAdapter.items = curSchedule
                scheduleAdapter.notifyDataSetChanged()
//                adapter.scheduleDate.text = curSchedule[0].date
//                scheduleTitle.text = curSchedule[0].title
//                scheduleContent.text = curSchedule[0].content
                //swipeLayout.isSwipeEnabled = true
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

                curDate = year.toString().plus("-") + (month + 1).toString()
                        .plus("-") + dayOfMonth.toString()

                //val curSchedule = viewModel.getScheduleByDate(curDate)
                val curSchedule = viewModel.scheduleList.filter {
                    it.date == curDate
                } as MutableList<Schedule>

                if (!curSchedule.isNullOrEmpty()) {
                    scheduleAdapter.items = curSchedule
                    scheduleAdapter.notifyDataSetChanged()
//                    scheduleDate.text = curSchedule!![0].date
//                    scheduleTitle.text = curSchedule!![0].title
//                    scheduleContent.text = curSchedule!![0].content
                    //swipeLayout.isSwipeEnabled = true
                    //swipeLayout.open(swipeLayout.dragEdge)
                } else {
//                    scheduleDate.text = ""
//                    scheduleTitle.text = ""
//                    scheduleContent.text = ""
                    //ScheduleRecyclerView.visibility = View.GONE
                    //swipeLayout.isSwipeEnabled = false
                    scheduleAdapter.items = mutableListOf()
                    scheduleAdapter.notifyDataSetChanged()
                }
            }

            addBtn.setOnClickListener {
                if (nameEdit.text.toString() == "")
                    Toast.makeText(requireContext(), "일정을 입력해주세요.", Toast.LENGTH_SHORT).show()
                else {
                    var dDay = dDayCheck.isChecked

                    val newSchedule = Schedule(
                            date = curDate,
                            title = nameEdit.text.toString(),
                            content = scheduleEdit.text.toString(),
                            dDay = dDay
                    )
                    viewModel.insertSchedule(newSchedule)

                    nameEdit.text.clear()
                    scheduleEdit.text.clear()

                    addBtn.visibility = View.GONE
                    cancelBtn.visibility = View.GONE
                    dDayCheck.visibility = View.GONE
                    addSchedule.visibility = View.GONE
                    addScheduleBtn.visibility = View.VISIBLE
                    swipeLayout.visibility = View.VISIBLE
                    Toast.makeText(binding!!.root.context, "일정을 추가했습니다.", Toast.LENGTH_SHORT).show()

                    scheduleAdapter.addItem(newSchedule)
                    if(dDay) dDayAdapter.addItem(newSchedule)
                }
            }

            dDayAdapter = DdayAdapter(viewModel.scheduleList.filter { it.dDay } as MutableList<Schedule>)
            val simpleCallBack = object : ItemTouchHelper.SimpleCallback(
                    ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                    ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                        recyclerView: RecyclerView,
                        viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder
                ): Boolean {
                    dDayAdapter.moveItem(viewHolder.adapterPosition, target.adapterPosition)
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val schedule = dDayAdapter.removeItem(viewHolder.adapterPosition)
                    viewModel.deleteScheduleByDateAndTitle(schedule.date, schedule.title)

                    Toast.makeText(binding!!.root.context, "일정을 삭제하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallBack)
            itemTouchHelper.attachToRecyclerView(DdayRecyclerView)

            dDayAdapter.itemClickListener = object : DdayAdapter.OnItemClickListener {
                override fun onDateClick(holder: DdayAdapter.ViewHolder, view: View, data: Schedule) {
                    calendarView.date = dDayAdapter.date.time

                    if (dDayAdapter.hasContent) {
//                        scheduleDate.text = viewModel.scheduleList[adapter.curPos].date
//                        scheduleTitle.text = viewModel.scheduleList[adapter.curPos].title
//                        scheduleContent.text = viewModel.scheduleList[adapter.curPos].content
                        //swipeLayout.isSwipeEnabled = true
                        scheduleAdapter.items = viewModel.scheduleList.filter {
                            it.date == data.date
                        } as MutableList<Schedule>
                        scheduleAdapter.notifyDataSetChanged()
                        swipeLayout.open(swipeLayout.dragEdge)
                    }
                }
            }
            DdayRecyclerView.adapter = dDayAdapter
        }
    }

}