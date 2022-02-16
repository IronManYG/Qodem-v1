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
import androidx.viewpager2.widget.ViewPager2
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
import com.example.qodem.ui.CampaignBloodBankAdapter
import com.example.qodem.ui.InfographicViewPagerAdapter
import com.example.qodem.ui.appointment.AppointmentLocationFragmentDirections
import com.example.qodem.utils.CustomCountDownTimer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(), CampaignBloodBankAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    //
    private lateinit var campaignBloodBankAdapter: CampaignBloodBankAdapter
    private lateinit var infographicViewPagerAdapter: InfographicViewPagerAdapter

    //
    private lateinit var _bloodBanks: List<BloodBank>

    //
    private lateinit var _activeDonation: Donation

    //
    private var dayToEnableVerify by Delegates.notNull<Int>()
    private var hoursToEnableVerify by Delegates.notNull<Int>()
    private var minuteToEnableVerify by Delegates.notNull<Int>()
    private var secondsToEnableVerify by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_home, container, false
            )

        //
        setupRecyclerView()
        setupInfographicViewPager()

        //
        binding.buttonBookAnAppointment.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToPreScreeningRequestFragment(-1)
            findNavController().navigate(action)
        }

        //
        binding.buttonCancelAppointment.setOnClickListener {
            binding.layoutProgressDonation.visibility = View.VISIBLE
            binding.layoutAppointmentDetails.visibility = View.GONE
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                    viewModel.updateDonationActiveState(_activeDonation.id, false)
                }
            }
        }

        //
        binding.buttonVerifyAppointment.setOnClickListener {
            if(dayToEnableVerify == 0 && hoursToEnableVerify == 0 && minuteToEnableVerify <= 30 && secondsToEnableVerify <= 59){
                val amount = _activeDonation.id
                val action = HomeFragmentDirections.actionHomeFragmentToAuthenticationAppointmentFragment(amount)
                findNavController().navigate(action)
            } else {
                Toast.makeText(requireActivity(), "It can only be done on Appointment time.", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner) { bloodBanks ->
            val campaignBloodBanks: MutableList<BloodBank> = mutableListOf()
            // Show only campaign blood banks
            for (bloodBank in bloodBanks) {
                if (bloodBank.bloodDonationCampaign) {
                    campaignBloodBanks.add(bloodBank)
                }
            }

            campaignBloodBankAdapter.bloodBanks = campaignBloodBanks
            infographicViewPagerAdapter.infographics = listOf(
                "https://raw.githubusercontent.com/IronManYG/Qodem/master/app/src/main/res/drawable/infographic1.jpg",
                "https://raw.githubusercontent.com/IronManYG/Qodem/master/app/src/main/res/drawable/infographic2.jpg",
                "https://raw.githubusercontent.com/IronManYG/Qodem/master/app/src/main/res/drawable/infographic3.jpg",
                "https://raw.githubusercontent.com/IronManYG/Qodem/master/app/src/main/res/drawable/infographic4.jpg"
            )

            //
            updateAppointmentState(bloodBanks)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    //
    private fun setupRecyclerView() = binding.recyclerViewDonationCampaigns.apply {
        campaignBloodBankAdapter = CampaignBloodBankAdapter(this@HomeFragment)
        adapter = campaignBloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    //
    private fun setupInfographicViewPager() = binding.viewPagerInfographic.apply {
        infographicViewPagerAdapter = InfographicViewPagerAdapter()
        adapter = infographicViewPagerAdapter
    }

    private fun updateAppointmentState(bloodBanks: List<BloodBank>) {

        viewModel.activeDonationFoundState.observe(viewLifecycleOwner) {
            //
            binding.layoutProgressDonation.visibility = View.VISIBLE
            binding.layoutAppointmentDetails.visibility = View.GONE
            binding.layoutNoAppointment.visibility = View.GONE
            when (it) {
                true -> {

                    //
                    viewModel.donation.observe(viewLifecycleOwner) { donations ->
                        for (activeDonation in donations) {
                            if (activeDonation.active) {
                                _activeDonation = activeDonation

                                //
                                val donationDateCountDownTimer =
                                    CustomCountDownTimer(activeDonation.donationDataTimeStamp)
                                donationDateCountDownTimer.start()

                                //
                                var activeDonationBloodBank: BloodBank? = null

                                //
                                for (bloodBank in bloodBanks) {
                                    if (bloodBank.id.toString() == activeDonation.bloodBankID) {
                                        activeDonationBloodBank = bloodBank
                                    }
                                }

                                //
                                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                Log.d("timeStamp 2", "${activeDonation.donationDataTimeStamp}")
                                calendar.time = Date(activeDonation.donationDataTimeStamp)
                                calendar.add(Calendar.MONTH, 1)

                                val dayOfWeekString =
                                    calendar.getDisplayName(
                                        Calendar.DAY_OF_WEEK,
                                        Calendar.LONG,
                                        Locale.ENGLISH
                                    )

                                val monthString =
                                    calendar.getDisplayName(
                                        Calendar.MONTH,
                                        Calendar.LONG,
                                        Locale.ENGLISH
                                    )

                                val amPmString =
                                    calendar.getDisplayName(
                                        Calendar.AM_PM,
                                        Calendar.LONG,
                                        Locale.ENGLISH
                                    )

                                val donationDate = "$dayOfWeekString, " +
                                        "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                                        "$monthString " +
                                        "${calendar.get(Calendar.YEAR)}"
                                val donationTime =
                                    "${String.format("%02d", calendar.get(Calendar.HOUR))}:" +
                                            String.format("%02d", calendar.get(Calendar.MINUTE)) +
                                            " $amPmString"
                                // Publish & update Appointment info
                                binding.textAppointmentPlace.text = activeDonationBloodBank?.name_en
                                binding.textAppointmentCity.text = activeDonationBloodBank?.city
                                binding.textAppointmentDate.text = donationDate
                                binding.textAppointmentTime.text = donationTime
                                donationDateCountDownTimer.countDownDays.observe(viewLifecycleOwner) { remainingDays ->
                                    binding.textRemainingDaysField.text =
                                        String.format("%02d", remainingDays.toInt())
                                    dayToEnableVerify = remainingDays.toInt()
                                }
                                donationDateCountDownTimer.countDownHours.observe(viewLifecycleOwner) { remainingHours ->
                                    binding.textRemainingHoursField.text =
                                        String.format("%02d", remainingHours.toInt())
                                    hoursToEnableVerify = remainingHours.toInt()
                                }
                                donationDateCountDownTimer.countDownMinutes.observe(viewLifecycleOwner) { remainingMinutes ->
                                    binding.textRemainingMinutesField.text =
                                        String.format("%02d", remainingMinutes.toInt())
                                    minuteToEnableVerify = remainingMinutes.toInt()
                                }
                                donationDateCountDownTimer.countDownSeconds.observe(viewLifecycleOwner) { remainingSeconds ->
                                    binding.textRemainingSecondsField.text =
                                        String.format("%02d", remainingSeconds.toInt())
                                    secondsToEnableVerify = remainingSeconds.toInt()
                                }
                            }
                        }
                    }
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
        }
    }

    override fun onItemClick(position: Int) {
        val campaignBloodBank = campaignBloodBankAdapter.bloodBanks[position]
        val amount = campaignBloodBank.id
        val action = HomeFragmentDirections.actionHomeFragmentToPreScreeningRequestFragment(amount)
        findNavController().navigate(action)
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