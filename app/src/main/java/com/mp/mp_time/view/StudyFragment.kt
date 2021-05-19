package com.mp.mp_time.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mp.mp_time.R
import com.mp.mp_time.databinding.FragmentStudyBinding

class StudyFragment : Fragment() {
    var binding: FragmentStudyBinding? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudyBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

}