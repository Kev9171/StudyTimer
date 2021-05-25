package com.mp.mp_time.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.adapter.TestAdapter
import com.mp.mp_time.data.Test
import com.mp.mp_time.databinding.FragmentCalendarBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.util.*

class CalendarFragment : Fragment() {
    var binding: FragmentCalendarBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    lateinit var adapter: TestAdapter
    //private val ONE_DAY = 24 * 60 * 60 * 1000

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentCalendarBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding!!.apply {
            DdayRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = TestAdapter(viewModel.testList)

            addTestBtn.setOnClickListener {
                if (DdayRecyclerView.visibility == View.VISIBLE) {
                    DdayRecyclerView.visibility = View.GONE
                    addTestBtn.visibility = View.GONE
                    addBtn.visibility = View.VISIBLE
                    cancelBtn.visibility = View.VISIBLE
                    addTestName.visibility = View.VISIBLE
                } else {
                    addBtn.visibility = View.GONE
                    cancelBtn.visibility = View.GONE
                    addTestName.visibility = View.GONE
                    addTestBtn.visibility = View.VISIBLE
                    DdayRecyclerView.visibility = View.VISIBLE
                }
            }

            calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->

                addBtn.setOnClickListener {
                    if(nameEdit.text.toString()=="")
                        Toast.makeText(requireContext(), "과목명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                    else {
                        viewModel.addTest(Test(name = nameEdit.text.toString(),
                                dyear = year, dmonth = month, dday = dayOfMonth))
                        addBtn.visibility = View.GONE
                        cancelBtn.visibility = View.GONE
                        addTestName.visibility = View.GONE
                        addTestBtn.visibility = View.VISIBLE
                        DdayRecyclerView.visibility = View.VISIBLE
                        //addTestBtn.performClick()
                    }
                }
                cancelBtn.setOnClickListener {
                    addBtn.visibility = View.GONE
                    cancelBtn.visibility = View.GONE
                    addTestName.visibility = View.GONE
                    addTestBtn.visibility = View.VISIBLE
                    DdayRecyclerView.visibility = View.VISIBLE
                    //addTestBtn.performClick()
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
                    adapter.removeItem(viewHolder.adapterPosition)
                }
            }
            val itemTouchHelper = ItemTouchHelper(simpleCallBack)
            itemTouchHelper.attachToRecyclerView(DdayRecyclerView)

            adapter.itemClickListener = object : TestAdapter.OnItemClickListener {
                override fun onDateClick(holder: TestAdapter.ViewHolder, view: View, data: Test) {

                }
            }

            DdayRecyclerView.adapter = adapter

        }
    }

}