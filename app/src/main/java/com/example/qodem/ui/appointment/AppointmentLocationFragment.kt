package com.example.qodem.ui.appointment

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentAppointmentLocationBinding
import com.example.qodem.ui.BloodBankAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppointmentLocationFragment : Fragment(), BloodBankAdapter.OnItemClickListener{

    companion object {
        const val TAG = "AppointmentLocationFrag"
    }

    private val viewModel: AppointmentLocationViewModel by viewModels()

    private lateinit var binding: FragmentAppointmentLocationBinding

    private var isBloodBankSelected = false

    //
    private lateinit var bloodBankAdapter: BloodBankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAppointmentLocationBinding.inflate(layoutInflater)

        //
        setupRecyclerView()

        binding.buttonNextStep.setOnClickListener {
            if (isBloodBankSelected) {
                Toast.makeText(requireActivity(), "You select blood bank", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireActivity(), "Please select blood bank", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner) { bloodBanks ->
            //
            bloodBankAdapter.bloodBanks = bloodBanks
        }
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewBloodBanks.apply {
        bloodBankAdapter = BloodBankAdapter(this@AppointmentLocationFragment)
        adapter = bloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onItemClick(position: Int) {
        bloodBankAdapter.bloodBanks.forEach{
            it.isSelected = it == bloodBankAdapter.bloodBanks[position]
        }
        isBloodBankSelected = true
    }

    override fun onPhoneNumberImageClick(position: Int) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + bloodBankAdapter.bloodBanks[position].phoneNumber)
        startActivity(dialIntent)
        bloodBankAdapter.notifyItemChanged(position)
    }

    override fun onBloodBankPlaceImageClick(position: Int) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" +
                "${bloodBankAdapter.bloodBanks[position].coordinates.latitude}," +
                "${bloodBankAdapter.bloodBanks[position].coordinates.longitude}" +
                "(${bloodBankAdapter.bloodBanks[position].name_en})"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

}