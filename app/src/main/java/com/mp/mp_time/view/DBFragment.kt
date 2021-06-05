package com.mp.mp_time.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.databinding.FragmentDbBinding
import com.mp.mp_time.viewmodel.StudyViewModel

class DBFragment : Fragment() {
    var binding: FragmentDbBinding? = null
    val viewModel: StudyViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDbBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var str = "<<과목 리스트>>\n"
        viewModel.subjectList.forEach {
            str += it.subName +
                    " / " + it.isPage +
                    " / " + it.goalInt +
                    " / " + it.studyTime +
                    " / " + it.breakTime +
                    " / " + it.date +
                    " / " + it.achievedTime + "\n"
        }
        binding!!.subjectResultTmp.text = str

        var str2 = "<<일정 리스트>>\n"
        //viewModel.deleteScheduleByDateAndTitle("2020-01-01", "모프 강의")
        viewModel.findAllSchedule()!!.forEach {
            str2 += it.date +
                    " / " + it.title +
                    " / " + it.content +
                    " / " + it.dDay + "\n"
        }
        binding!!.scheduleResultTmp.text = str2
    }
}