package com.mp.mp_time.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R
import com.mp.mp_time.adapter.SubjectAdapter
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.FragmentStudyBinding
import com.mp.mp_time.viewmodel.StudyViewModel

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null
    val viewModel: StudyViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding!!.apply {
            subjectRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val adapter = SubjectAdapter(mutableListOf<Subject>())
            subjectRecyclerView.adapter = adapter

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
            itemTouchHelper.attachToRecyclerView(subjectRecyclerView)

            adapter.itemClickListener = object : SubjectAdapter.OnItemClickListener {
                override fun onTimerClick(holder: SubjectAdapter.ViewHolder, view: View, data: Subject, position: Int) {
                    // TimerFragment 로 전환
                    Toast.makeText(requireContext(), "Timer 시작", Toast.LENGTH_SHORT).show()
                }
            }

            // 테스트를 위한 임시 값들
            adapter.addItem(Subject("모프", "1:00:00", "12주차 강의", "30%"))
            adapter.addItem(Subject("운체", "3:04:10", "11주차 강의", "50%"))
            adapter.addItem(Subject("c프", "1:00:00", "12주차 강의", "30%"))
            adapter.addItem(Subject("java", "1:00:00", "12주차 강의", "30%"))
            adapter.addItem(Subject("동아리", "1:00:00", "1주차 강의", "80%"))
            adapter.addItem(Subject("리액트", "1:00:00", "0주차 강의", "30%"))
            adapter.addItem(Subject("알고리즘", "1:00:00", "강의", "10%"))
            adapter.addItem(Subject("자료구조", "1:00:00", "강의", "10%"))
            adapter.addItem(Subject("팀플", "1:00:00", "12주차 강의", "10%"))


        }
    }

}