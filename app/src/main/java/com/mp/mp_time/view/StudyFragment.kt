package com.mp.mp_time.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.R
import com.mp.mp_time.databinding.FragmentStudyBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.util.*
import kotlin.concurrent.timer

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    private var time = 40
    private var timerTask : Timer? = null
    private var iswork = false
    private var rate = 0
    private val maxtime = time

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(layoutInflater, container, false)

        binding!!.graph.max = time

        binding!!.countdown.text = "${String.format("%02d", time / 60)} : ${String.format("%02d", time % 60)}"

        binding!!.play.setOnClickListener {

            if (iswork == false){
                binding!!.play.setImageResource(R.drawable.ic_baseline_pause_24)
                startTimer()
                iswork = true
            }
            else {
                binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                stopTimer()
                iswork = false
            }

        }



        return binding!!.root
    }

    private fun stopTimer() {
        timerTask?.cancel()
    }

    private fun startTimer() {
        timerTask = timer(period = 1000) {
            time--

            rate = maxtime - time


            var sec = time % 60
            var min = time / 60




            activity?.runOnUiThread {

                binding!!.countdown.text = "${String.format("%02d", min)} : ${String.format("%02d", sec)}"
                binding!!.graph.progress = rate

            }

        }
    }

}