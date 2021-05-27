package com.mp.mp_time.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.data.Subject
import com.mp.mp_time.databinding.FragmentAddSubjectBinding
import com.mp.mp_time.viewmodel.StudyViewModel

class AddSubjectFragment : Fragment() {
    var binding: FragmentAddSubjectBinding? = null
    val viewModel: StudyViewModel by activityViewModels()

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
            addBtn.setOnClickListener {
                if(inputSubjectName.text.toString() == ""){
                    Toast.makeText(requireContext(), "과목명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else {
                    // 과목 등록
                    viewModel.addSubject(Subject(
                        subName = inputSubjectName.text.toString()
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