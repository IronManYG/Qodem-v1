package com.example.qodem.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.ui.BloodBankAdapter
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: FragmentHomeBinding

    //d
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

        //d
        setupRecyclerView()

//        subscribeObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.bloodBanksList.observe(viewLifecycleOwner,{ bloodBanks ->
            bloodBankAdapter.bloodBanks = bloodBanks
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

    // d
    private fun setupRecyclerView() = binding.recyclerViewDonationCampaigns.apply {
        bloodBankAdapter = BloodBankAdapter()
        adapter = bloodBankAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

//    private fun subscribeObservers() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.dataState.collect { dataState ->
//                when (dataState) {
//                    is DataState.Success<List<BloodBank>> -> {
//                        displayProgressBar(false)
//                        bloodBankAdapter.bloodBanks = dataState.data
//                    }
//                    is DataState.Error -> {
//                        displayProgressBar(false)
//                        displayError(dataState.exception.message)
//                    }
//                    is DataState.Loading -> {
//                        displayProgressBar(true)
//                    }
//                }
//            }
//        }
//    }

    private fun displayError(message: String?) {
        if (message != null) {
            Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        } else Toast.makeText(requireActivity(), "Unknown error", Toast.LENGTH_LONG).show()
    }

    private fun displayProgressBar(isDisplayed: Boolean) {
        binding.progressBar.visibility = if (isDisplayed) View.VISIBLE else View.GONE
    }

}