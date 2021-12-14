package com.example.qodem.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qodem.R
import com.example.qodem.data.remote.RetrofitInstance
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.ui.BloodBankAdapter
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.example.qodem.utils.DataState
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.common.config.GservicesValue.value
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import retrofit2.HttpException
import java.io.IOException

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
        const val TAG = "HomeFragment"
    }

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

        binding.signOutButton.setOnClickListener{ onClick(binding.signOutButton) }

        //d
//        lifecycleScope.launchWhenCreated {
//            binding.progressBar.isVisible = true
//            val response = try {
//                RetrofitInstance.RETROFIT.get()
//            } catch(e: IOException) {
//                Log.e(TAG, "IOException, you might not have internet connection")
//                binding.progressBar.isVisible = false
//                return@launchWhenCreated
//            } catch (e: HttpException) {
//                Log.e(TAG, "HttpException, unexpected response")
//                binding.progressBar.isVisible = false
//                return@launchWhenCreated
//            }
//            if(response.isSuccessful && response.body() != null) {
//                bloodBankAdapter.bloodBanks = response.body()!!
//            } else {
//                Log.e(TAG, "Response not successful")
//            }
//            binding.progressBar.isVisible = false
//        }

        subscribeObservers()
        viewModel.setStateEvent(MainStateEvent.GetBloodBanksEvent)

        return binding.root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        _viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun onClick(v: View) {
        if (v.id == R.id.sign_out_button) {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                    activity?.finish()
                }
        }
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

    private fun subscribeObservers(){
        viewModel.dataState.observe(requireActivity(), Observer{ dataState ->
            when (dataState){
                is DataState.Success<List<BloodBank>> -> {
                    displayProgressBar(false)
                    bloodBankAdapter.bloodBanks = dataState.data
                }
                is DataState.Error -> {
                    displayProgressBar(false)
                    displayError(dataState.exception.message)
                }
                is DataState.Loading -> {
                    displayProgressBar(true)
                }
            }

        })
    }

    private fun displayError(message: String?){
        if(message != null)  {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
        } else Toast.makeText(requireActivity(), "Unknown error", Toast.LENGTH_LONG).show()
    }

    private fun displayProgressBar(isDisplayed: Boolean){
        binding.progressBar.visibility = if(isDisplayed) View.VISIBLE else View.GONE
    }

}