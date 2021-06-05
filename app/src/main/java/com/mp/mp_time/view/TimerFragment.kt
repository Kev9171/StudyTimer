package com.mp.mp_time.view

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.R
import com.mp.mp_time.databinding.FragmentTimerBinding
import com.mp.mp_time.viewmodel.StudyViewModel
import java.util.*
import kotlin.concurrent.timer

class TimerFragment : Fragment() {
    var binding: FragmentTimerBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    val inputtotalstudytime = 40
    val inputstudy = 10
    val inputrest = 10

    private var time = inputtotalstudytime
    private var study = inputstudy
    private var rest  = inputrest

    private var timerTask : Timer? = null
    private var iswork  = false
    private var rate = 0
    private val maxtime  = time

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater, container, false)

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

        if(binding!!.menualButton.isChecked) {



            timerTask = timer(period = 1000) {

                if (time == 0) {
                    stopTimer()

                    activity?.runOnUiThread {
                        binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    iswork = false
                }

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

        else if(binding!!.autoButton.isChecked) {



            timerTask = timer(period = 1000) {

                if (time == 0) {
                    stopTimer()

                    activity?.runOnUiThread {
                        binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    iswork = false
                }

                if(study > 0) {
                    time--
                    study--
                    rate = maxtime - time


                    var sec = time % 60
                    var min = time / 60




                    activity?.runOnUiThread {

                        binding!!.countdown.text = "${String.format("%02d", min)} : ${String.format("%02d", sec)}"
                        binding!!.graph.progress = rate

                    }

                }

                else {

                    rest--
                    activity?.runOnUiThread {

                        binding!!.resttime.text = "남은 휴식 시간 : ${String.format("%02d", rest / 60)} : ${String.format("%02d", rest % 60)}"

                    }

                    if(rest == 0){

                        study = inputstudy
                        rest = inputrest
                        activity?.runOnUiThread {

                            binding!!.resttime.text = ""

                        }


                    }
                }








            }

        }


    }

    override fun onPause() {
        super.onPause()

        if(iswork) {
            binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            stopTimer()
            iswork = false
        }

        makeNotification()

    }

    fun makeNotification(){
        //알림발생
        val id = "MyChannel"
        val name = "TimerChannel"
        val notificationChannel = NotificationChannel(id,name, NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.enableVibration(true)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

        val builder = NotificationCompat.Builder(requireActivity(),id)
                .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
                .setContentTitle("타이머 중지")
                .setContentText("클릭시 타이머로 이동")
                .setAutoCancel(true)

        //알람 누르면 화면 전환
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.putExtra("time", "123")
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val PendingIntent = PendingIntent.getActivity(requireActivity(),1,intent, PendingIntent.FLAG_UPDATE_CURRENT)
        builder.setContentIntent(PendingIntent)

        val manager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(notificationChannel)

        val Notification = builder.build()

        manager.notify(10, Notification)

    }



}