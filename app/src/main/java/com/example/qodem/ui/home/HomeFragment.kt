package com.example.qodem.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.ui.BloodBankAdapter
import com.example.qodem.ui.CampaignBloodBankAdapter
import com.example.qodem.utils.CustomCountDownTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    //
    private lateinit var campaignBloodBankAdapter: CampaignBloodBankAdapter

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

        binding.buttonBookAnAppointment.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_preScreeningRequestFragment)
        }

        //
        setupRecyclerView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner,{ bloodBanks ->
            val campaignBloodBanks: MutableList<BloodBank> = mutableListOf()
            // Show only campaign blood banks
            for (bloodBank in bloodBanks){
                if (bloodBank.bloodDonationCampaign){
                    campaignBloodBanks.add(bloodBank)
                }
            }
            campaignBloodBankAdapter.bloodBanks = campaignBloodBanks
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
        campaignBloodBankAdapter = CampaignBloodBankAdapter()
        adapter = campaignBloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)
    }

    private fun updateAppointmentState(bloodBanks: List<BloodBank>) {
        //
        viewModel.activeDonationFoundState.observe(viewLifecycleOwner,{
            when (it) {
                true -> {

                    //
                    viewModel.activeDonation.observe(viewLifecycleOwner,{ activeDonation ->

                        //
                        val donationDateCountDownTimer = CustomCountDownTimer(activeDonation.timeStamp)
                        donationDateCountDownTimer.start()

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

                        val dayOfWeekString = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)

                        val monthString = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)

                        val amPmString = calendar.getDisplayName(Calendar.AM_PM, Calendar.LONG, Locale.ENGLISH)

                        val donationDate = "$dayOfWeekString, " +
                                "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                                "$monthString " +
                                "${calendar.get(Calendar.YEAR)}"
                        val donationTime = "${calendar.get(Calendar.HOUR)}:" +
                                "${calendar.get(Calendar.MINUTE)}"+
                                " $amPmString"
                        // Publish & update Appointment info
                        binding.textAppointmentPlace.text = activeDonationBloodBank!!.name_en
                        binding.textAppointmentCity.text = activeDonationBloodBank.city
                        binding.textAppointmentDate.text = donationDate
                        binding.textAppointmentTime.text = donationTime
                        donationDateCountDownTimer.countDownDays.observe(viewLifecycleOwner,{ remainingDays ->
                            binding.textRemainingDaysField.text = remainingDays
                        })
                        donationDateCountDownTimer.countDownHours.observe(viewLifecycleOwner,{ remainingHours ->
                            binding.textRemainingHoursField.text = remainingHours
                        })
                        donationDateCountDownTimer.countDownMinutes.observe(viewLifecycleOwner,{ remainingMinutes ->
                            binding.textRemainingMinutesField.text = remainingMinutes
                        })
                        donationDateCountDownTimer.countDownSeconds.observe(viewLifecycleOwner,{ remainingSeconds ->
                            binding.textRemainingSecondsField.text = remainingSeconds
                        })

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