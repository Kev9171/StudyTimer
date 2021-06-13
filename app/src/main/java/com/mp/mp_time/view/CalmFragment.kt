package com.mp.mp_time.view

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R
import com.mp.mp_time.adapter.CalmAdapter
import com.mp.mp_time.data.MusicData
import java.util.*
import kotlin.collections.ArrayList


class CalmFragment : Fragment() {


    var data1: ArrayList<MusicData> = ArrayList()
    var music1: ArrayList<MediaPlayer> = ArrayList()
    lateinit var recyclerView1: RecyclerView
    lateinit var adapter1: CalmAdapter

    private var playing:Int = -1

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
        val view = inflater.inflate(R.layout.fragment_calm, container, false)
        initData()
        initRecyclerView(view)



        return view
    }

    private fun initRecyclerView(view:View) {
        recyclerView1 = view.findViewById(R.id.calmRecyclerView)
        recyclerView1.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter1 = CalmAdapter(data1)

        val dividerItemDecoration = DividerItemDecoration(recyclerView1.context, LinearLayoutManager(activity).orientation)
        recyclerView1.addItemDecoration(dividerItemDecoration)


        adapter1.itemClickListener = object :  CalmAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, mdata: MusicData, position: Int) {
                val mActivity =  activity as MusicPlayerActivity



                if(mActivity.isplay == 0){
                    player(position)
                    adapter1?.notifyDataSetChanged()
                }else{
                    if(mActivity.selected==1){
                        player(position)
                        adapter1?.notifyDataSetChanged()
                    }else{
                        Toast.makeText(activity,"다른 탭에서 노래가 실행중입니다.",Toast.LENGTH_SHORT).show()
                    }
                }





            }

        }
        recyclerView1.adapter=adapter1
    }





    private fun initData() {
        data1.add(MusicData("calm1", "조용한음악1",false))
        data1.add(MusicData("calm2", "조용한음악2",false))
        data1.add(MusicData("calm3", "조용한음악3",false))
        data1.add(MusicData("calm4", "조용한음악4",false))
        music1.add(MediaPlayer.create(activity, R.raw.calm1))
        music1.add(MediaPlayer.create(activity, R.raw.calm2))
        music1.add(MediaPlayer.create(activity, R.raw.calm3))
        music1.add(MediaPlayer.create(activity, R.raw.calm4))

    }



    fun player(position:Int){

        val mActivity =  activity as MusicPlayerActivity



        if(position==playing){
            if(mActivity.mediaPlayer.isPlaying){
                data1[position].flag=false
                mActivity.mediaPlayer.pause()
                playing=-1
                adapter1?.notifyDataSetChanged()
                mActivity.isplay=0
                return
            }
        }else{
            if(playing!=-1){
                data1[playing].flag=false
                mActivity.mediaPlayer.pause()
                playing = position
                data1[playing].flag=true
                sendToAct(playing)
                adapter1?.notifyDataSetChanged()
                mActivity.isplay=1

            }else{
                playing = position
                data1[playing].flag=true
                sendToAct(playing)
                adapter1?.notifyDataSetChanged()
                mActivity.isplay=1

            }
        }
    }

    fun sendToAct(pos:Int){
        val mActivity =  activity as MusicPlayerActivity
        mActivity.receiveData(music1[pos],1, pos)
    }



    override fun onStop() {
        if(playing!=-1){
            music1[playing].stop()
        }
        super.onStop()
    }


}