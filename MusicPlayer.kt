package com.mp.mp_time

import android.media.MediaPlayer
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.tabs.TabLayoutMediator
import com.mp.mp_time.databinding.ActivityMusicPlayerBinding

class MusicPlayer : AppCompatActivity() {

    lateinit var playingTime:TextView
    lateinit var duration:TextView
    lateinit var seekBar: SeekBar
    lateinit var runnable: Runnable
    lateinit var handler: Handler
    lateinit var mediaPlayer: MediaPlayer



    lateinit var binding: ActivityMusicPlayerBinding
    val textarr = arrayListOf<String>("잔잔한","평화로운","우울한")
    val iconarr = arrayListOf<Int>(R.drawable.calm_image,R.drawable.peace_image,R.drawable.sad_image)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pager()
        //receiveData(MediaPlayer.create(this, R.raw.calm1))
    }

    fun pager() {
        binding.viewPager.adapter=MyFragStateAdapter(this)
        TabLayoutMediator(binding.tablayout, binding.viewPager){
                tab, position ->
            tab.text = textarr[position]
            tab.setIcon(iconarr[position])
        }.attach()
    }




    fun receiveData(mPlayer: MediaPlayer){

        playingTime = binding.time
        duration = binding.end
        seekBar = binding.seekBar
        handler = Handler()
        //mediaPlayer = MediaPlayer.create(this, R.raw.calm1)
        mediaPlayer = mPlayer
        mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener{
            override fun onPrepared(mp: MediaPlayer?) {
                seekBar.max = mediaPlayer.duration
                mediaPlayer.start()
                SeekBar()
            }
        })
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    mediaPlayer.seekTo(progress)
                    mediaPlayer.start()
                    SeekBar()
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

    }

    fun SeekBar(){
        seekBar.setProgress(mediaPlayer.currentPosition)
        val min:Int = mediaPlayer.duration/60000
        val sec:Int = (mediaPlayer.duration-60000*min)/1000
        val min2:Int = mediaPlayer.currentPosition/60000
        val sec2:Int = (mediaPlayer.currentPosition-60000*min2)/1000
        playingTime.text = ""+min2+":"+sec2+""
        duration.text = ""+min+":"+sec+""
        if(mediaPlayer.isPlaying){
            runnable = Runnable {
                SeekBar()
            }
            handler.postDelayed(runnable,1000)
        }
    }


}