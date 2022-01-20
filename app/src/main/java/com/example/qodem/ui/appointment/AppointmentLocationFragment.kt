package com.example.qodem.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.qodem.R
import com.example.qodem.databinding.FragmentAppointmentLocationBinding
import com.example.qodem.databinding.FragmentPreScreeningQuestionBinding

class AppointmentLocationFragment : Fragment() {

    private lateinit var binding: FragmentAppointmentLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentLocationBinding.inflate(layoutInflater)

        return binding.root
    }

}