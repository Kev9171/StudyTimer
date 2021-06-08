package com.mp.mp_time.view

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.findFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.android.gms.location.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mp.mp_time.databinding.FragmentAddPlaceBinding
import com.mp.mp_time.viewmodel.StudyViewModel

class AddPlaceFragment : Fragment(){
    var binding: FragmentAddPlaceBinding? = null
    val viewModel: StudyViewModel by activityViewModels()
    var lat : Double = 0.0
    var lng : Double = 0.0
    lateinit var geocoder: Geocoder
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
        binding = FragmentAddPlaceBinding.inflate(layoutInflater, container, false)





        geocoder = Geocoder(requireActivity())



        binding!!.map


        binding!!.apply {
            ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->

                viewModel.AddPlaceNow!!.rating = ratingBar.rating

            }

            searchPlaceBtn.setOnClickListener {

                if(inputLocation.text.toString() == "")
                    Toast.makeText(requireContext(), "주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                else {
                    lat = geocoder.getFromLocationName(inputLocation.text.toString(), 1)[0].latitude
                    lng = geocoder.getFromLocationName(inputLocation.text.toString(), 1)[0].longitude


                    //지도에 마커로 보여주기

                    init()





                    viewModel.AddPlaceNow!!.location = LatLng(lat, lng)
                }
            }

            addBtn.setOnClickListener {

                if(inputPlaceName.text.toString() == "")
                    Toast.makeText(requireContext(), "장소명을 입력해주세요.", Toast.LENGTH_SHORT).show()
                else if(inputComment.text.toString() == "")
                    Toast.makeText(requireContext(), "코멘트를 입력해주세요.", Toast.LENGTH_SHORT).show()
                else if(inputLocation.text.toString() == "")
                    Toast.makeText(requireContext(), "주소를 입력해주세요.", Toast.LENGTH_SHORT).show()
                else {
                    viewModel.AddPlaceNow!!.placeName = inputPlaceName.text.toString()
                    viewModel.AddPlaceNow!!.comment = inputComment.text.toString()

                    //이후 DB에 추가

                    // insert(place(viewModel.AddPlaceNow!!.placeName,
                    // viewModel.AddPlaceNow!!.location,
                    // viewModel.AddPlaceNow!!.comment,
                    // viewModel.AddPlaceNow!!.rating))

                    requireActivity().supportFragmentManager
                            .beginTransaction()
                            .remove(this@AddPlaceFragment)
                            .commit()
                    requireActivity().supportFragmentManager.popBackStack()
                }


            }
        }








        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }




    private fun init() {





        mapFragment = childFragmentManager.findFragmentById(binding!!.map.id) as SupportMapFragment
        mapFragment.getMapAsync {
            googleMap = it

            googleMap.clear()

            option = MarkerOptions()

            option.position(LatLng(lat, lng))

            option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

            googleMap.addMarker(option)

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 16.0f))

        }





            /*mapFragment = SupportMapFragment.newInstance()
            mapFragment.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(p0: GoogleMap) {
                    val option = MarkerOptions()

                    option.position(LatLng(34.665731, 135.432746))

                    option.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))

                    p0.addMarker(option)
                    p0.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(34.665731, 135.432746), 16.0f))
                }

            })





        childFragmentManager.beginTransaction()
                .replace(R.id.map , mapFragment)
                .commit()*/


    }



}