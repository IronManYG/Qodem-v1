package com.example.qodem.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
import com.example.qodem.ui.BloodBankAdapter
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.example.qodem.ui.signup.SignUpActivity
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    //
    private lateinit var bloodBankAdapter: BloodBankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )

        //
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner,{ bloodBanks ->
            //
            bloodBankAdapter.bloodBanks = bloodBanks
            //
            updateAppointmentState(bloodBanks)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewDonationCampaigns.apply {
        bloodBankAdapter = BloodBankAdapter()
        adapter = bloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateAppointmentState(bloodBanks: List<BloodBank>) {
        //
        viewModel.activeDonationFoundState.observe(viewLifecycleOwner,{
            when (it) {
                true -> {

                    //
                    viewModel.activeDonation.observe(viewLifecycleOwner,{ activeDonation ->

                        //
                        var activeDonationBloodBank: BloodBank? = null

                        //
                        for(bloodBank in bloodBanks) {
                            if(bloodBank.id.toString() == activeDonation.bloodBankID) {
                                activeDonationBloodBank = bloodBank
                            }
                        }

                        //
                        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                        Log.d("timeStamp 2", "${activeDonation.timeStamp}")
                        calendar.time = Date(activeDonation.timeStamp)
                        calendar.add(Calendar.MONTH, 1)
                        val donationDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                                "${calendar.get(Calendar.MONTH)}/" +
                                "${calendar.get(Calendar.YEAR)}"
                        val donationTime = "${calendar.get(Calendar.HOUR)}:" +
                                "${calendar.get(Calendar.MINUTE)}"
                        //
                        binding.textAppointmentPlace.text = activeDonationBloodBank!!.name_en
                        binding.textAppointmentCity.text = activeDonationBloodBank.city
                        binding.textAppointmentDate.text = donationDate
                        binding.textAppointmentTime.text = donationTime


                    })

                    //
                    binding.layoutProgressDonation.visibility = View.GONE
                    binding.layoutAppointmentDetails.visibility = View.VISIBLE

                }
                false -> {
                    //
                    binding.layoutProgressDonation.visibility = View.GONE
                    binding.layoutNoAppointment.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        } else Toast.makeText(requireActivity(), "Unknown error", Toast.LENGTH_LONG).show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}