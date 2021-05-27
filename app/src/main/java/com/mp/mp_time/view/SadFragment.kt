package com.mp.mp_time.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R
import com.mp.mp_time.adapter.CalmAdapter
import com.mp.mp_time.data.MusicData


class SadFragment : Fragment() {



    var data: ArrayList<MusicData> = ArrayList()
    var music: ArrayList<MediaPlayer> = ArrayList()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CalmAdapter

    var playing:Int = -1

    lateinit var playingTime: TextView
    lateinit var duration: TextView
    lateinit var seekBar: SeekBar
    lateinit var runnable: Runnable
    lateinit var handler: Handler
    lateinit var mediaPlayer: MediaPlayer






    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sad, container, false)
        initData()
        initRecyclerView(view)




        return view
    }

    private fun initRecyclerView(view:View) {
        recyclerView = view.findViewById(R.id.sadRecyclerView)
        recyclerView.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter = CalmAdapter(data)

        val dividerItemDecoration = DividerItemDecoration(recyclerView.context, LinearLayoutManager(activity).orientation)
        recyclerView.addItemDecoration(dividerItemDecoration)


        adapter.itemClickListener = object :  CalmAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, mdata: MusicData, position: Int) {

                player(position)
                adapter?.notifyDataSetChanged()

            }

        }
        recyclerView.adapter=adapter
    }

    private fun initData() {
        data.add(MusicData("sad1", "슬픈음악1",false))
        data.add(MusicData("sad2", "슬픈음악2",false))
        data.add(MusicData("sad3", "슬픈음악3",false))
        data.add(MusicData("sad4", "슬픈음악4",false))
        music.add(MediaPlayer.create(activity, R.raw.sad1))
        music.add(MediaPlayer.create(activity, R.raw.sad2))
        music.add(MediaPlayer.create(activity, R.raw.sad3))
        music.add(MediaPlayer.create(activity, R.raw.sad4))
    }

    fun player(position:Int){

        if(position==playing){
            if(music[position].isPlaying){
                data[position].flag=false
                music[playing].pause()
                playing=-1
                return
            }
        }else{
            if(playing!=-1){
                data[playing].flag=false
                music[playing].pause()
                playing = position
                data[playing].flag=true
                music[playing].start()
                //sendToAct(playing)
            }else{
                playing = position
                data[playing].flag=true
                music[playing].start()
                //sendToAct(playing)
            }
        }
    }

    fun sendToAct(pos:Int){
        val mActivity =  activity as MusicPlayerActivity
        mActivity.receiveData(music[pos])
    }



    override fun onStop() {
        if(playing!=-1){
            music[playing].stop()
        }
        super.onStop()
    }


}