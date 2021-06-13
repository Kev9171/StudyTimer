package com.mp.mp_time.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mp.mp_time.adapter.MyPlaceAdapter
import com.mp.mp_time.data.Place
import com.mp.mp_time.databinding.FragmentMyPlaceBinding
import com.mp.mp_time.viewmodel.FragmentRequest
import com.mp.mp_time.viewmodel.StudyViewModel

class MyPlaceFragment : Fragment() {
    var binding: FragmentMyPlaceBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    lateinit var adapter:MyPlaceAdapter
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var option: MarkerOptions

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
            adapter = MyPlaceAdapter(viewModel.placeList)

            adapter.itemClickListener = object : MyPlaceAdapter.OnItemClickListener{
                override fun onItemClick(holder: MyPlaceAdapter.ViewHolder, view: View, data: Place, position: Int) {
                   initmap(data.location)
                }

            }

            placeRecyclerView.adapter = adapter

            addPlaceBtn.setOnClickListener {
                viewModel.fragmentTranslationRequest(FragmentRequest.REQUEST_PLACE)
            }
        }
    }

    private fun initmap(latLng: LatLng) {


        mapFragment = childFragmentManager.findFragmentById(binding!!.map.id) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it

            googleMap.clear()

            option = MarkerOptions()

            option.position(latLng)

            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            googleMap.addMarker(option)

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))

        }
    }
}