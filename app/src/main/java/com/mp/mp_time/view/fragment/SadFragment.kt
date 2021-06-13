package com.mp.mp_time.view.fragment

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mp.mp_time.R
import com.mp.mp_time.adapter.CalmAdapter
import com.mp.mp_time.data.MusicData
import com.mp.mp_time.view.activity.MusicPlayerActivity


class SadFragment : Fragment() {


    var data3: ArrayList<MusicData> = ArrayList()
    var music3: ArrayList<MediaPlayer> = ArrayList()
    lateinit var recyclerView3: RecyclerView
    lateinit var adapter3: CalmAdapter

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
        val view = inflater.inflate(R.layout.fragment_sad, container, false)
        initData()
        initRecyclerView(view)
        //init()




        return view
    }

    private fun initRecyclerView(view:View) {
        recyclerView3 = view.findViewById(R.id.sadRecyclerView)
        recyclerView3.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter3 = CalmAdapter(data3)

        val dividerItemDecoration = DividerItemDecoration(recyclerView3.context, LinearLayoutManager(activity).orientation)
        recyclerView3.addItemDecoration(dividerItemDecoration)


        adapter3.itemClickListener = object :  CalmAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, mdata: MusicData, position: Int) {

                val mActivity =  activity as MusicPlayerActivity

                if(mActivity.isplay == 0){
                    player(position)
                    adapter3?.notifyDataSetChanged()
                }else{
                    if(mActivity.selected==3){
                        player(position)
                        adapter3?.notifyDataSetChanged()
                    }else{
                        Toast.makeText(activity,"다른 탭에서 노래가 실행중입니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        recyclerView3.adapter=adapter3
    }





    private fun initData() {
        data3.add(MusicData("sad1", "슬픈음악1",false))
        data3.add(MusicData("sad2", "슬픈음악2",false))
        data3.add(MusicData("sad3", "슬픈음악3",false))
        data3.add(MusicData("sad4", "슬픈음악4",false))
        music3.add(MediaPlayer.create(activity, R.raw.sad1))
        music3.add(MediaPlayer.create(activity, R.raw.sad2))
        music3.add(MediaPlayer.create(activity, R.raw.sad3))
        music3.add(MediaPlayer.create(activity, R.raw.sad4))
    }

    fun player(position:Int){
        val mActivity =  activity as MusicPlayerActivity

        if(position==playing){
            if(mActivity.mediaPlayer.isPlaying){
                data3[position].flag=false
                mActivity.mediaPlayer.pause()
                playing=-1
                adapter3?.notifyDataSetChanged()
                mActivity.isplay=0
                return
            }
        }else{
            if(playing!=-1){
                data3[playing].flag=false
                mActivity.mediaPlayer.pause()
                playing = position
                data3[playing].flag=true
                sendToAct(playing)
                adapter3?.notifyDataSetChanged()
                mActivity.isplay=1
            }else{
                playing = position
                data3[playing].flag=true
                sendToAct(playing)
                adapter3?.notifyDataSetChanged()
                mActivity.isplay=1
            }
        }
    }

    fun sendToAct(pos:Int){
        val mActivity =  activity as MusicPlayerActivity
        mActivity.receiveData(music3[pos],3,pos)
    }



    override fun onStop() {
        if(playing!=-1){
            music3[playing].stop()
        }
        super.onStop()
    }


}