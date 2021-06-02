package com.mp.mp_time.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mp.mp_time.databinding.FragmentMyPlaceBinding
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
}