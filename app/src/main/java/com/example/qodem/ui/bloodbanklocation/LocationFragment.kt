package com.example.qodem.ui.bloodbanklocation

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.qodem.R
import com.example.qodem.databinding.FragmentLocationBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.utils.DataState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LocationFragment : Fragment() {

    private val viewModel: LocationViewModel by viewModels()

    private lateinit var binding: FragmentLocationBinding

    private var bloodBanks: List<BloodBank> = emptyList()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once theb
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationBinding.inflate(layoutInflater)

//        subscribeObservers()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        lifecycleScope.launchWhenCreated {
//            Log.d("here","start map")
//            // Get map
//            val googleMap = mapFragment.awaitMap()
//
//            // Wait for map to finish loading
//            googleMap.awaitMapLoad()
//
//            // Ensure all places are visible in the map
//            val bounds = LatLngBounds.builder()
//            bloodBanks.forEach { bounds.include(it.coordinates) }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
//
//            addMarkers(googleMap)
//        }
        mapFragment?.getMapAsync(callback)
    }

//    private fun subscribeObservers() {
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.dataState.collect { dataState ->
//                when (dataState) {
//                    is DataState.Success<List<BloodBank>> -> {
//                        displayProgressBar(false)
//                        bloodBanks = dataState.data
//                        Log.d("here","${bloodBanks.size}")
//                        Log.d("here","${dataState.data.size}")
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

    /**
     * Adds markers to the map. These markers won't be clustered.
     */
    private fun addMarkers(googleMap: GoogleMap) {
        bloodBanks.forEach { bloodBank ->
            val marker = googleMap.addMarker {
                title(bloodBank.name_en)
                position(bloodBank.coordinates)
            }
            // Set place as the tag on the marker object so it can be referenced within
            // MarkerInfoWindowAdapter
            if (marker != null) {
                marker.tag = bloodBank
            }
        }
    }
}