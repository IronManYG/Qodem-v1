package com.example.qodem.ui.appointment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.databinding.FragmentAppointmentDateBinding
import com.example.qodem.ui.AppointmentDataAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentDateFragment : Fragment(), AppointmentDataAdapter.OnItemClickListener {

    companion object {
        const val TAG = "AppointmentDateFrag"
    }

    private val viewModel: AppointmentLocationViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentDateBinding

    //
    private lateinit var appointmentDataAdapter: AppointmentDataAdapter

    private val args: AppointmentDateFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentDateBinding.inflate(layoutInflater)

        //
        setupRecyclerView()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner) { bloodBanks ->
            //
            appointmentDataAdapter.bloodBanks = bloodBanks
        }
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewDaySelector.apply {
        appointmentDataAdapter = AppointmentDataAdapter(this@AppointmentDateFragment)
        adapter = appointmentDataAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(position: Int) {
        TODO("Not yet implemented")
    }

}