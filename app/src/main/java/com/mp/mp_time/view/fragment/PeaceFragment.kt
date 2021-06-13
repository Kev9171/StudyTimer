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


class PeaceFragment : Fragment() {


    var data2: ArrayList<MusicData> = ArrayList()
    var music2: ArrayList<MediaPlayer> = ArrayList()
    lateinit var recyclerView2: RecyclerView
    lateinit var adapter2: CalmAdapter

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
        val view = inflater.inflate(R.layout.fragment_peace, container, false)
        initData()
        initRecyclerView(view)
        //init()




        return view
    }

    private fun initRecyclerView(view:View) {
        recyclerView2 = view.findViewById(R.id.peaceRecyclerView)
        recyclerView2.layoutManager= LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false)
        adapter2 = CalmAdapter(data2)

        val dividerItemDecoration = DividerItemDecoration(recyclerView2.context, LinearLayoutManager(activity).orientation)
        recyclerView2.addItemDecoration(dividerItemDecoration)


        adapter2.itemClickListener = object :  CalmAdapter.OnItemClickListener{
            override fun OnItemClick(holder: RecyclerView.ViewHolder, view: View, mdata: MusicData, position: Int) {

                val mActivity =  activity as MusicPlayerActivity

                if(mActivity.isplay == 0){
                    player(position)
                    adapter2?.notifyDataSetChanged()
                }else{
                    if(mActivity.selected==2){
                        player(position)
                        adapter2?.notifyDataSetChanged()
                    }else{
                        Toast.makeText(activity,"다른 탭에서 노래가 실행중입니다.",Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
        recyclerView2.adapter=adapter2
    }



    private fun initData() {
        data2.add(MusicData("peace1", "평화로운음악1",false))
        data2.add(MusicData("peace2", "평화로운음악2",false))
        data2.add(MusicData("peace3", "평화로운음악3",false))
        data2.add(MusicData("peace4", "평화로운음악4",false))
        music2.add(MediaPlayer.create(activity, R.raw.peace1))
        music2.add(MediaPlayer.create(activity, R.raw.peace2))
        music2.add(MediaPlayer.create(activity, R.raw.peace3))
        music2.add(MediaPlayer.create(activity, R.raw.peace4))
    }

    fun player(position:Int){
        val mActivity =  activity as MusicPlayerActivity

        if(position==playing){
            if(mActivity.mediaPlayer.isPlaying){
                data2[position].flag=false
                mActivity.mediaPlayer.pause()
                playing=-1
                adapter2?.notifyDataSetChanged()
                mActivity.isplay=0
                return
            }
        }else{
            if(playing!=-1){
                data2[playing].flag=false
                mActivity.mediaPlayer.pause()
                playing = position
                data2[playing].flag=true
                sendToAct(playing)
                adapter2?.notifyDataSetChanged()
                mActivity.isplay=1
            }else{
                playing = position
                data2[playing].flag=true
                sendToAct(playing)
                adapter2?.notifyDataSetChanged()
                mActivity.isplay=1
            }
        }
    }

    fun sendToAct(pos:Int){
        val mActivity =  activity as MusicPlayerActivity
        mActivity.receiveData(music2[pos],2,pos)
    }



    override fun onStop() {
        if(playing!=-1){
            music2[playing].stop()
        }
        super.onStop()
    }


}