package com.mp.mp_time.view

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.mp.mp_time.R
import com.mp.mp_time.adapter.MyFragStateAdapter
import com.mp.mp_time.databinding.ActivityMusicPlayerBinding
import java.util.*

class MusicPlayerActivity : AppCompatActivity() {

    lateinit var playingTime:TextView
    lateinit var dura:TextView
    lateinit var seekBar: SeekBar
    lateinit var runnable: Runnable
    lateinit var handler: Handler
    lateinit var mediaPlayer: MediaPlayer

    var selected:Int = -2
    var mpos:Int = -1
    var isplay = 0



    lateinit var binding: ActivityMusicPlayerBinding
    val textarr = arrayListOf<String>("잔잔한","평화로운","우울한")
    val iconarr = arrayListOf<Int>(R.drawable.calm_image, R.drawable.peace_image, R.drawable.sad_image)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pager()
        init()
        //receiveData(MediaPlayer.create(this, R.raw.calm1))
    }

    fun pager() {
        binding.viewPager.adapter= MyFragStateAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewPager){
                tab, position ->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()
    }




    private fun init(){
        binding.startbtn.setOnClickListener {
            if(!mediaPlayer.isPlaying){
                mediaPlayer.start()
                SeekBar(mediaPlayer)
                isplay = 1
            }
        }
        binding.pausebtn.setOnClickListener {
            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
                isplay = 0
            }
        }
        binding.stopbtn.setOnClickListener {

            if(mediaPlayer.isPlaying){
                mediaPlayer.pause()
            }
            mediaPlayer.seekTo(0)
            SeekBar(mediaPlayer)
            isplay = 0


        }
    }





    fun receiveData(mPlayer: MediaPlayer, FragNum:Int, Mpos:Int){
        selected = FragNum
        mpos = Mpos

        playingTime = binding.time
        dura = binding.end
        seekBar = binding.seekBar
        handler = Handler()
        mediaPlayer = mPlayer
        isplay = 1
//        mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
//            override fun onPrepared(mp: MediaPlayer?) {
//                seekBar.max = mediaPlayer.duration
//                mediaPlayer.start()
//                SeekBar(mediaPlayer)
//            }
//        })


        seekBar.max = mediaPlayer.duration
        mediaPlayer.start()
        SeekBar(mediaPlayer)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    binding.seekBar.max = mediaPlayer.duration
                    Toast.makeText(this@MusicPlayerActivity,"${seekBar!!.max}",Toast.LENGTH_SHORT).show()
                    mediaPlayer.seekTo(progress)
                    mediaPlayer.start()
                    SeekBar(mediaPlayer)
                    isplay = 1
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

    }

    fun SeekBar(mediaPlayer: MediaPlayer){
        seekBar.setProgress(mediaPlayer.currentPosition)
        val min:Int = mediaPlayer.duration/60000
        val sec:Int = (mediaPlayer.duration-60000*min)/1000
        val min2:Int = mediaPlayer.currentPosition/60000
        val sec2:Int = (mediaPlayer.currentPosition-60000*min2)/1000
        playingTime.text = ""+min2+":"+sec2+""
        dura.text = ""+min+":"+sec+""
        if(mediaPlayer.isPlaying){
            runnable = Runnable {
                SeekBar(mediaPlayer)
            }
            handler.postDelayed(runnable,1000)
        }
    }


}