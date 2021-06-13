package com.mp.mp_time.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.FragmentAddSubjectBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddSubjectFragment : Fragment() {
    var binding: FragmentAddSubjectBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    var isPage: Boolean = true

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddSubjectBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding!!.apply {
            // 과목명 빼고는 선택 하지 않으면 디폴트 값으로 설정

            // 페이지 or 시간 선택 Spinner
            val isPageAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    mutableListOf("페이지", "시간"))
            isPageSpinner.adapter = isPageAdapter
            isPageSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    isPage = position == 0 // 페이지 선택
                    if(isPage) isPageTextView.text = "페이지"
                    else isPageTextView.text = "시간"
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

            addBtn.setOnClickListener {
                if(inputSubjectName.text.toString() == ""){
                    Toast.makeText(requireContext(), "과목명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else {
                    // 이미 등록한 과목인지 확인
                    val checkSubject = viewModel.subjectList.find { it.subName == inputSubjectName.text.toString() }
                    if(checkSubject != null) {
                        Toast.makeText(requireContext(), "이미 등록한 과목입니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // 과목 등록
                    val studyTime: Float =
                            studyHourSpinner.selectedItem.toString().toInt() +
                                    studyMinSpinner.selectedItem.toString().toFloat() / 100
                    val breakTime: Float =
                            breakHourSpinner.selectedItem.toString().toInt() +
                                    breakMinSpinner.selectedItem.toString().toFloat() / 100

                    val date = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE) // 오늘 날짜. 'yyyy-mm-dd' 형식

                    viewModel.insertSubject(Subject(
                            subName = inputSubjectName.text.toString(),
                            isPage = isPage,
                            goalInt = inputGoalInt.text.toString().toInt(),
                            studyTime= studyTime,
                            breakTime= breakTime,
                            date = date,
                            achievedTime = "00:00:00"
                    ))

                    // fragment 종료
                    requireActivity().supportFragmentManager
                        .beginTransaction()
                        .remove(this@AddSubjectFragment)
                        .commit()
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

}