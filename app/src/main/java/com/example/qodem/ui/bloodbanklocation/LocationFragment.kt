package com.example.qodem.ui.bloodbanklocation

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.qodem.R
import com.example.qodem.databinding.FragmentLocationBinding
import com.example.qodem.model.BloodBank
import com.example.qodem.ui.appointment.AppointmentLocationFragmentDirections
import com.example.qodem.utils.BitmapHelper

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.addCircle
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class LocationFragment : Fragment() {

    private val viewModel: LocationViewModel by viewModels()

    private lateinit var binding: FragmentLocationBinding

    private var isBloodBankSelected = false

    //
    private var bloodBankID = -1

    private val bloodDropIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(requireContext(), R.color.primaryDarkColor)
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.ic_blood_drop, color)
    }

    private var circle: Circle? = null

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
//        val sydney = LatLng(-34.0, 151.0)
//        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        addClusteredMarkers(googleMap)

        // Ensure all places are visible in the map
        val bounds = LatLngBounds.builder()
        viewModel.bloodBanksList.observe(viewLifecycleOwner, Observer { bloodBanks ->
            bloodBanks.forEach {
                bounds.include(it.coordinates)
            }
        })

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))

        googleMap.setOnMapClickListener {
            circle?.remove()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationBinding.inflate(layoutInflater)

        binding.buttonBookAnAppointment.setOnClickListener {
            if (isBloodBankSelected) {
                viewModel.activeDonationFoundState.observe(viewLifecycleOwner){
                    when (it){
                        true -> {
                            Toast.makeText(requireActivity(), "You have a pre-booked appointment.", Toast.LENGTH_LONG).show()
                        }
                        false -> {
                            val amount = bloodBankID
                            val action = LocationFragmentDirections.actionLocationFragmentToPreScreeningRequestFragment(amount)
                            findNavController().navigate(action)
                        }
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Please select blood bank", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        lifecycleScope.launchWhenCreated {

            // Get map
            val googleMap = mapFragment.awaitMap()

            // Wait for map to finish loading
            googleMap.awaitMapLoad()
        }
        mapFragment.getMapAsync(callback)
    }

    /**
     * Adds markers to the map. These markers won't be clustered.
     */
    private fun addMarkers(googleMap: GoogleMap) {
        viewModel.bloodBanksList.observe(viewLifecycleOwner, Observer { bloodBanks ->
            bloodBanks.forEach { bloodBank ->
                val marker = googleMap.addMarker {
                    title(bloodBank.name_en)
                    position(bloodBank.coordinates)
                    icon(bloodDropIcon)
                }
                // Set place as the tag on the marker object so it can be referenced within
                // MarkerInfoWindowAdapter
                marker?.tag = bloodBank
            }
        })
    }

    /**
     * Adds markers to the map with clustering support.
     */
    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer.
        val clusterManager = ClusterManager<BloodBank>(requireContext(), googleMap)
        clusterManager.renderer =
            BloodBankRenderer(
                requireContext(),
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
//        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))

        // Show polygon
        clusterManager.setOnClusterItemClickListener { bloodBank ->
            updateBloodBankView(bloodBank)
            isBloodBankSelected = true
            bloodBankID = bloodBank.id
            //addCircle(googleMap, bloodBank)
            return@setOnClusterItemClickListener false
        }

        // Add the places to the ClusterManager.
        viewModel.bloodBanksList.observe(viewLifecycleOwner){ bloodBanks ->
            clusterManager.addItems(bloodBanks)
        }
        clusterManager.cluster()

        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            // When the camera stops moving, change the alpha value back to opaque.
            clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }

            // Call clusterManager.onCameraIdle() when the camera stops moving so that reclustering
            // can be performed when the camera stops moving.
            clusterManager.onCameraIdle()
        }

        // When the camera starts moving, change the alpha value of the marker to translucent.
        googleMap.setOnCameraMoveStartedListener {
            clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
            clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
        }
    }

    /**
     * Adds a [Circle] around the provided [item]
     */
    private fun addCircle(googleMap: GoogleMap, item: BloodBank) {
        circle?.remove()
        circle = googleMap.addCircle {
            center(item.coordinates)
            radius(1000.0)
            fillColor(ContextCompat.getColor(requireContext(), R.color.primaryLightColor))
            strokeColor(ContextCompat.getColor(requireActivity(), R.color.primaryColor))
        }
    }

    private fun onPhoneNumberImageClick(bloodBank: BloodBank) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:" + bloodBank.phoneNumber)
        startActivity(dialIntent)
    }

    private fun onBloodBankPlaceImageClick(bloodBank: BloodBank) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=" +
                "${bloodBank.coordinates.latitude}," +
                "${bloodBank.coordinates.longitude}" +
                "(${bloodBank.name_en})"
        )
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        mapIntent.resolveActivity(requireContext().packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    private fun updateBloodBankView(bloodBank: BloodBank){
        binding.layoutNoSelectedBloodBank.visibility = View.GONE
        binding.includeItmeBloodBank.root.visibility = View.VISIBLE
        binding.includeItmeBloodBank.root.strokeWidth = 5
        binding.cardView.strokeColor = ContextCompat.getColor(requireContext(), R.color.secondaryColor)
        binding.cardView.strokeWidth = 5
        binding.includeItmeBloodBank.textBloodBank.text = bloodBank.name_en
        binding.includeItmeBloodBank.imageBloodBankPlace.setOnClickListener{
            onBloodBankPlaceImageClick(bloodBank)
        }
        binding.includeItmeBloodBank.imagePhoneNumber.setOnClickListener{
            onPhoneNumberImageClick(bloodBank)
        }
    }
}