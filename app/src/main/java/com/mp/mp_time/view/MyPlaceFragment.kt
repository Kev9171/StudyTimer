package com.mp.mp_time.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.mp.mp_time.adapter.MyPlaceAdapter
import com.mp.mp_time.adapter.SubjectAdapter
import com.mp.mp_time.data.Place
import com.mp.mp_time.databinding.FragmentMyPlaceBinding
import com.mp.mp_time.viewmodel.FragmentRequest
import com.mp.mp_time.viewmodel.StudyViewModel

class MyPlaceFragment : Fragment() {
    var binding: FragmentMyPlaceBinding? = null
    val viewModel: StudyViewModel by activityViewModels()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyPlaceBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        binding!!.apply {
            placeRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            val adapter = MyPlaceAdapter(mutableListOf())

            // test data
            adapter.addItem(Place("도서관", LatLng(10.0,10.0), "", 4.2f))
            adapter.addItem(Place("스터디 카페", LatLng(10.0,10.0), "", 3.0f))
            adapter.addItem(Place("1917", LatLng(10.0,10.0), "", 5.0f))

            placeRecyclerView.adapter = adapter

            addPlaceBtn.setOnClickListener {
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_PLACE)
            }
        }
    }
}