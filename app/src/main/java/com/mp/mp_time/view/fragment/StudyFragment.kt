package com.mp.mp_time.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.adapter.SubjectAdapter
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.FragmentStudyBinding
import com.mp.mp_time.viewmodel.FragmentRequest
import com.mp.mp_time.viewmodel.StudyViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    val adapter: SubjectAdapter by lazy { SubjectAdapter(mutableListOf()) }

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

        val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식
        viewModel.findTodaySubjects(date)

        init()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.todaySubjectList.observe(viewLifecycleOwner) {
            if(it!=null) adapter.changeItems(it)
        }
    }

    private fun init() {
        binding!!.apply {
            subjectRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

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
                    viewModel.timerSubjectNow = data // 해당 과목 view model 에 전달
                    viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_TIMER)
                    //Toast.makeText(requireContext(), "Timer 시작", Toast.LENGTH_SHORT).show()
                }

                override fun onItemClick(
                    holder: SubjectAdapter.ViewHolder,
                    view: View,
                    data: Subject,
                    position: Int
                ) {
                    // ModifySubjectFragment 로 전환
                    viewModel.modifySubjectNow = data // 해당 과목 view model 에 전달
                    viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_MODIFY)
                    //Toast.makeText(requireContext(), "과목 수정", Toast.LENGTH_SHORT).show()
                }
            }

            subjectRecyclerView.adapter = adapter

            addSubjectBtn.setOnClickListener {
                // 과목 등록
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_SUBJECT)
            }
        }
    }

}