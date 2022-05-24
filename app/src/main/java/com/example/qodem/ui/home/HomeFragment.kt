package com.example.qodem.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.model.Donation
import com.example.qodem.ui.CampaignBloodBankAdapter
import com.example.qodem.ui.InfographicViewPagerAdapter
import com.example.qodem.utils.ConnectionLiveData
import com.example.qodem.utils.CustomCountDownTimer
import com.example.qodem.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), CampaignBloodBankAdapter.OnItemClickListener {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    //
    private lateinit var campaignBloodBankAdapter: CampaignBloodBankAdapter
    private lateinit var infographicViewPagerAdapter: InfographicViewPagerAdapter

    //
    private lateinit var bloodBankList: List<BloodBank>

    //
    private lateinit var _activeDonation: Donation

    //
    private lateinit var connectionLiveData: ConnectionLiveData

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
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home,
            container,
            false
        )

        setupConnectionChecker()
        setupRecyclerView()
        setupInfographicViewPager()

        binding.apply {
            //
            buttonBookAnAppointment.setOnClickListener {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToPreScreeningRequestFragment(-1)
                findNavController().navigate(action)
            }

            //
            buttonCancelAppointment.setOnClickListener {
                layoutProgressDonation.visibility = View.VISIBLE
                layoutAppointmentDetails.visibility = View.GONE
                CoroutineScope(Dispatchers.IO).launch {
                    withContext(Dispatchers.IO) {
                        viewModel.updateDonationActiveState(_activeDonation.id, false)
                    }
                }
            }

            //
            buttonVerifyAppointment.setOnClickListener {
                if (dayToEnableVerify == 0 && hoursToEnableVerify == 0 && minuteToEnableVerify <= 30 && secondsToEnableVerify <= 59) {
                    val amount = _activeDonation.id
                    val action =
                        HomeFragmentDirections.actionHomeFragmentToAuthenticationAppointmentFragment(
                            amount
                        )
                    findNavController().navigate(action)
                } else {
                    root.showSnackbar(
                        root,
                        "It can only be done on Appointment time.",
                        Snackbar.LENGTH_LONG,
                        null,
                        requireContext()
                    ) {}
                }
            }

            //
            imageAppointmentDirections.setOnClickListener {
                onAppointmentPlaceImageClick(_activeDonation.bloodBankID.toInt())
            }

            imageAppointmentQr.setOnClickListener {
                root.showSnackbar(
                    root,
                    "Feature under development.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }

            //
            buttonFindDonors.setOnClickListener {
                root.showSnackbar(
                    root,
                    "Feature under development.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }
            buttonAddDonor.setOnClickListener {
                root.showSnackbar(
                    root,
                    "Feature under development.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }
            buttonSnapAndShare.setOnClickListener {
                root.showSnackbar(
                    root,
                    "Feature under development.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }
            buttonBloodJourney.setOnClickListener {
                root.showSnackbar(
                    root,
                    "Feature under development.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bloodBanksList.collect { bloodBanks ->
                    bloodBankList = bloodBanks
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    private fun setupRecyclerView() = binding.recyclerViewDonationCampaigns.apply {
        campaignBloodBankAdapter = CampaignBloodBankAdapter(this@HomeFragment)
        adapter = campaignBloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupInfographicViewPager() = binding.viewPagerInfographic.apply {
        infographicViewPagerAdapter = InfographicViewPagerAdapter()
        adapter = infographicViewPagerAdapter
        binding.indicator.setViewPager(this)
        infographicViewPagerAdapter.registerAdapterDataObserver(binding.indicator.adapterDataObserver)
        binding.imageDragRight.setOnClickListener {
            this.apply {
                beginFakeDrag()
                fakeDragBy(-12f)
                endFakeDrag()
            }
        }
        binding.imageDragLeft.setOnClickListener {
            this.apply {
                beginFakeDrag()
                fakeDragBy(12f)
                endFakeDrag()
            }
        }
    }

    private fun setupConnectionChecker() {
        connectionLiveData = ConnectionLiveData(requireContext())

        connectionLiveData.observe(viewLifecycleOwner) { isNetworkAvailable ->
            when (isNetworkAvailable) {
                true -> {
                    viewModel.getBloodBanks()
                }
                false -> {
                    binding.root.showSnackbar(
                        binding.root,
                        "Network Not Available",
                        Snackbar.LENGTH_LONG,
                        null,
                        requireContext()
                    ) {}
                }
            }
        }
    }

    private fun updateAppointmentState(bloodBanks: List<BloodBank>) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.activeDonationFoundState.collect {
                    //
                    binding.layoutProgressDonation.visibility = View.VISIBLE
                    binding.layoutAppointmentDetails.visibility = View.GONE
                    binding.layoutNoAppointment.visibility = View.GONE
                    when (it) {
                        true -> {

                            //
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.donation.collect() { donations ->
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
                                                val calendar =
                                                    Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                                                Log.d(
                                                    "timeStamp 2",
                                                    "${activeDonation.donationDataTimeStamp}"
                                                )
                                                calendar.time =
                                                    Date(activeDonation.donationDataTimeStamp)
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
                                                val donationTime = "${
                                                    String.format(
                                                        "%02d",
                                                        calendar.get(Calendar.HOUR)
                                                    )
                                                }:" +
                                                        String.format(
                                                            "%02d",
                                                            calendar.get(Calendar.MINUTE)
                                                        ) +
                                                        " $amPmString"
                                                // Publish & update Appointment info
                                                binding.textAppointmentPlace.text =
                                                    activeDonationBloodBank?.name_en
                                                binding.textAppointmentCity.text =
                                                    activeDonationBloodBank?.city
                                                binding.textAppointmentDate.text = donationDate
                                                binding.textAppointmentTime.text = donationTime
                                                launch {
                                                    donationDateCountDownTimer.countDownDays.collect { remainingDays ->
                                                        binding.textRemainingDaysField.text =
                                                            String.format(
                                                                "%02d",
                                                                remainingDays.toInt()
                                                            )
                                                        dayToEnableVerify =
                                                            remainingDays.toInt()
                                                    }
                                                }
                                                launch {
                                                    donationDateCountDownTimer.countDownHours.collect() { remainingHours ->
                                                        binding.textRemainingHoursField.text =
                                                            String.format(
                                                                "%02d",
                                                                remainingHours.toInt()
                                                            )
                                                        hoursToEnableVerify =
                                                            remainingHours.toInt()
                                                    }
                                                }
                                                launch {
                                                    donationDateCountDownTimer.countDownMinutes.collect() { remainingMinutes ->
                                                        binding.textRemainingMinutesField.text =
                                                            String.format(
                                                                "%02d",
                                                                remainingMinutes.toInt()
                                                            )
                                                        minuteToEnableVerify =
                                                            remainingMinutes.toInt()
                                                    }
                                                }
                                                launch {
                                                    donationDateCountDownTimer.countDownSeconds.collect() { remainingSeconds ->
                                                        binding.textRemainingSecondsField.text =
                                                            String.format(
                                                                "%02d",
                                                                remainingSeconds.toInt()
                                                            )
                                                        secondsToEnableVerify =
                                                            remainingSeconds.toInt()
                                                    }
                                                }
                                            }
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
        }
    }

    private fun onAppointmentPlaceImageClick(position: Int) {
        val gmmIntentUri = Uri.parse(
            "geo:0,0?q=" +
                    "${bloodBankList[position - 1].coordinates.latitude}," +
                    "${bloodBankList[position - 1].coordinates.longitude}" +
                    "(${bloodBankList[position - 1].name_en})"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    override fun onItemClick(position: Int) {
        val campaignBloodBank = campaignBloodBankAdapter.bloodBanks[position]
        val amount = campaignBloodBank.id
        val action = HomeFragmentDirections.actionHomeFragmentToPreScreeningRequestFragment(amount)
        findNavController().navigate(action)
    }

    override fun onPhoneNumberImageClick(position: Int) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data =
            Uri.parse("tel:" + campaignBloodBankAdapter.bloodBanks[position].phoneNumber)
        startActivity(dialIntent)
    }

    override fun onBloodBankPlaceImageClick(position: Int) {
        val gmmIntentUri = Uri.parse(
            "geo:0,0?q=" +
                    "${campaignBloodBankAdapter.bloodBanks[position].coordinates.latitude}," +
                    "${campaignBloodBankAdapter.bloodBanks[position].coordinates.longitude}" +
                    "(${campaignBloodBankAdapter.bloodBanks[position].name_en})"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(mapIntent)
        }
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