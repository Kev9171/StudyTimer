package com.mp.mp_time.view.fragment

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.R
import com.mp.mp_time.databinding.FragmentTimerBinding
import com.mp.mp_time.view.activity.MainActivity
import com.mp.mp_time.viewmodel.StudyViewModel
import java.util.*
import kotlin.concurrent.timer

class TimerFragment : Fragment(){
    private lateinit var callback: OnBackPressedCallback
    var binding: FragmentTimerBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    val inputtotalstudytime = 40
    val inputstudy = 10
    val inputrest = 10


    private var floatact = false




    private var time = inputtotalstudytime
    private var study = 60
    private var rest  = inputrest

    private var timerTask : Timer? = null
    private var iswork  = false
    private var rate = 0
    private var maxtime  = 0

    private var usingtime = 0

    private var hour = 0
    private var min = 0
    private var sec = 0


    override fun onDestroyView() {


        if(!floatact && !viewModel.backpressact) {
            viewModel.recreate = true
            viewModel.time = time
            viewModel.maxtime = maxtime
            viewModel.progress = rate
            viewModel.resttime = rest
            viewModel.studytime = study
            viewModel.usingtime = usingtime
        }
        super.onDestroyView()
        binding = null


    }





    override fun onStop() {
        super.onStop()

        viewModel.timeused = usingtime
        viewModel.updateSubjectAchievedTime(viewModel.timerSubjectNow!!, usingtime)

        //Toast.makeText(requireActivity(), viewModel.timeused.toString() + "초 경과", Toast.LENGTH_SHORT).show()
    }






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTimerBinding.inflate(layoutInflater, container, false)


        viewModel.backpressact = false
        floatact = false

        time = viewModel.timerSubjectNow!!.studyTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.studyTime - viewModel.timerSubjectNow!!.studyTime.toInt() ) * 6000).toInt()
        study = viewModel.timerSubjectNow!!.studyTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.studyTime - viewModel.timerSubjectNow!!.studyTime.toInt() ) * 6000).toInt()
        rest = viewModel.timerSubjectNow!!.breakTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.breakTime - viewModel.timerSubjectNow!!.breakTime.toInt() ) * 6000).toInt()
        maxtime = time





        if(viewModel.recreate){
            time = viewModel.time
            maxtime = viewModel.maxtime
            rest = viewModel.resttime
            study = viewModel.studytime
            usingtime = viewModel.usingtime

            if(viewModel.isauto){
                binding!!.autoButton.isChecked = true
            }
            else{
                binding!!.menualButton.isChecked = true
            }
            binding!!.graph.progress = viewModel.progress
            checkablef()
        }






        binding!!.textView5.text = viewModel.timerSubjectNow!!.subName

        binding!!.graph.max = maxtime

        min = time / 60
        hour = min / 60
        sec = time % 60
        min %= 60

        binding!!.countdown.text = "${String.format("%02d", hour)} : ${String.format("%02d", min)} : ${String.format("%02d", sec)}"


        binding!!.play.setOnClickListener {

            if (iswork == false){
                binding!!.play.setImageResource(R.drawable.ic_baseline_pause_24)
                startTimer()
                iswork = true
                checkablef()
            }
            else {
                binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                stopTimer()
                iswork = false
            }

        }

        binding!!.floatingActionButton.setOnClickListener {
            viewModel.recreate = false
            floatact = true
            /*requireActivity().supportFragmentManager.popBackStack()
            viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_STUDY)*/
            requireActivity().supportFragmentManager.popBackStack()

            val fragmentTranslation = requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.mainContainer, StudyFragment())

            fragmentTranslation.commit()
        }



        return binding!!.root
    }

    private fun stopTimer() {
        timerTask?.cancel()
    }

    private fun startTimer() {

        if(binding!!.menualButton.isChecked) {


            viewModel.isauto = false

            binding!!.resttime.text = ""


            timerTask = timer(period = 1000) {

                if (time == 0) {
                    stopTimer()

                    activity?.runOnUiThread {
                        binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    iswork = false
                }

                else {
                    time--
                    usingtime++

                    rate = maxtime - time


                    min = time / 60
                    hour = min / 60
                    sec = time % 60
                    min %= 60




                    activity?.runOnUiThread {

                        binding!!.countdown.text = "${String.format("%02d", hour)} : ${String.format("%02d", min)} : ${String.format("%02d", sec)}"
                        binding!!.graph.progress = rate

                    }
                }



            }


        }

        else if(binding!!.autoButton.isChecked) {

            viewModel.isauto = true

            timerTask = timer(period = 1000) {

                if (time == 0) {
                    /*stopTimer()

                    activity?.runOnUiThread {
                        binding!!.play.setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    }
                    iswork = false*/

                    time = viewModel.timerSubjectNow!!.studyTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.studyTime - viewModel.timerSubjectNow!!.studyTime.toInt() ) * 6000).toInt()
                    rate = maxtime - time


                    min = time / 60
                    hour = min / 60
                    sec = time % 60
                    min %= 60




                    activity?.runOnUiThread {

                        binding!!.countdown.text = "${String.format("%02d", hour)} : ${String.format("%02d", min)} : ${String.format("%02d", sec)}"
                        binding!!.graph.progress = rate

                    }



                }

                else {

                    if (study > 0) {
                        time--
                        usingtime++
                        study--
                        rate = maxtime - time


                        min = time / 60
                        hour = min / 60
                        sec = time % 60
                        min %= 60




                        activity?.runOnUiThread {

                            binding!!.countdown.text = "${String.format("%02d", hour)} : ${String.format("%02d", min)} : ${String.format("%02d", sec)}"
                            binding!!.graph.progress = rate

                        }

                    } else {

                        if(rest == viewModel.timerSubjectNow!!.breakTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.breakTime - viewModel.timerSubjectNow!!.breakTime.toInt() ) * 6000).toInt()){
                            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                            val r1 : Ringtone = RingtoneManager.getRingtone(requireActivity(), notification)
                            r1.play()
                        }
                        rest--

                        min = rest / 60
                        hour = min / 60
                        sec = rest % 60
                        min %= 60

                        activity?.runOnUiThread {

                            binding!!.resttime.text = "남은 휴식 시간 : ${String.format("%02d", hour)} : ${String.format("%02d", min)} : ${String.format("%02d", sec)}"

                        }

                        if (rest == 0) {

                            study = viewModel.timerSubjectNow!!.studyTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.studyTime - viewModel.timerSubjectNow!!.studyTime.toInt() ) * 6000).toInt()
                            rest = viewModel.timerSubjectNow!!.breakTime.toInt() * 3600 + ((viewModel.timerSubjectNow!!.breakTime - viewModel.timerSubjectNow!!.breakTime.toInt() ) * 6000).toInt()
                            activity?.runOnUiThread {

                                binding!!.resttime.text = ""

                            }


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



        if(!floatact && !viewModel.backpressact) {
            viewModel.recreate = true
            viewModel.time = time
            viewModel.maxtime = maxtime
            viewModel.progress = rate

            viewModel.resttime = rest
            viewModel.studytime = study
            viewModel.usingtime = usingtime

            makeNotification()
        }

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

    fun checkablet() {
        binding!!.menualButton.isEnabled = true
        binding!!.autoButton.isEnabled = true
    }

    fun checkablef() {
        binding!!.menualButton.isEnabled = false
        binding!!.autoButton.isEnabled = false
    }






}