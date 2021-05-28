package com.mp.mp_time.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.FragmentModifySubjectBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ModifySubjectFragment : Fragment() {
    var binding: FragmentModifySubjectBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    var isPage: Boolean = true
    lateinit var subject: Subject

    var studyTime = 0.0f
    var studyHour = 0
    var studyMin = 0
    var breakTime = 0.0f
    var breakHour = 0
    var breakMin = 0

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentModifySubjectBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subject = viewModel.modifySubjectNow!!
        studyTime = subject.studyTime
        breakTime = subject.breakTime

        studyHour = subject.studyTime.toInt()
        studyMin = ((subject.studyTime - studyHour) * 100).toInt()
        breakHour = subject.breakTime.toInt()
        breakMin = ((subject.breakTime - breakHour) * 100).toInt()

        init()
    }

    private fun init() {
        binding!!.apply {
            // 수정하려는 과목 정보로 설정
            subjectName.text = subject.subName

            // 페이지 or 시간 선택 Spinner
            val isPageAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    mutableListOf("페이지", "시간"))
            isPageSpinner.adapter = isPageAdapter
            isPageSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    isPage = position == 0 // 페이지 선택
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                }
            }

            // 시간과 분 선택 Spinner
            val hourSpinnerAdapter = ArrayAdapter<Int>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    mutableListOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15))
            val minSpinnerAdapter = ArrayAdapter<Int>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    mutableListOf(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55))
            studyHourSpinner.adapter = hourSpinnerAdapter
            studyMinSpinner.adapter = minSpinnerAdapter
            breakHourSpinner.adapter = hourSpinnerAdapter
            breakMinSpinner.adapter = minSpinnerAdapter

            studyHourSpinner.setSelection(studyHour)
            studyMinSpinner.setSelection(studyMin/5)
            breakHourSpinner.setSelection(breakHour)
            breakMinSpinner.setSelection(breakMin/5)

            // 과목 정보 수정
            modifyBtn.setOnClickListener {
                // Toast.makeText(requireContext(), ".", Toast.LENGTH_SHORT).show()
                // 과목 수정 사항 확인
                val newStudyTime: Float =
                        studyHourSpinner.selectedItem.toString().toInt() +
                                studyMinSpinner.selectedItem.toString().toFloat() / 100
                val newBreakTime: Float =
                        breakHourSpinner.selectedItem.toString().toInt() +
                                breakMinSpinner.selectedItem.toString().toFloat() / 100

                val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식


                // 과목 정보 수정
                viewModel.updateSubject()

                // fragment 종료
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .remove(this@ModifySubjectFragment)
                        .commit()
                requireActivity().supportFragmentManager.popBackStack()
            }

            // 과목 정보 삭제
            deleteBtn.setOnClickListener {
                viewModel.deleteSubjectByName(subName = subject.subName)
                Toast.makeText(requireContext(), "${subject.subName} 이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                // fragment 종료
                requireActivity().supportFragmentManager
                        .beginTransaction()
                        .remove(this@ModifySubjectFragment)
                        .commit()
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

}