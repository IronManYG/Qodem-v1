package com.example.qodem.ui.appointment

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.budiyev.android.codescanner.*
import com.example.qodem.databinding.FragmentAuthenticationAppointmentBinding
import com.example.qodem.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthenticationAppointmentFragment : Fragment() {

    companion object {
        const val TAG = "authAppointmentFrag"
    }

    private val viewModel: AuthenticationAppointmentViewModel by viewModels()

    private lateinit var layout: View

    private lateinit var binding: FragmentAuthenticationAppointmentBinding

    private lateinit var codeScanner: CodeScanner

    private val args: AuthenticationAppointmentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationAppointmentBinding.inflate(layoutInflater, container, false)
        layout = binding.mainLayout

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.donationUpdatedState.collect { UpdatedState ->
                    when (UpdatedState) {
                        true -> {
                            CoroutineScope(Dispatchers.IO).launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.getAllDonations()
                                }
                            }
                            findNavController().navigate(AuthenticationAppointmentFragmentDirections.actionToHomeFragment())
                            viewModel.resetDonationUpdatedState()
                        }
                        false -> {
                            binding.root.showSnackBar(
                                binding.root,
                                viewModel.updateErrorMessage.value.toString(),
                                Snackbar.LENGTH_SHORT,
                                null,
                                requireContext()
                            ) {}
                        }
                    }
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRequestPermission(layout)
        codeScanner()
    }

    private fun codeScanner() {
        codeScanner = CodeScanner(requireContext(), binding.scannerView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    if (it.text == "Donated by the user, authenticate the appointment") {
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.progressBar4.visibility = View.VISIBLE
                            withContext(Dispatchers.Main) {
                                viewModel.updateDonationAuthenticatedState(
                                    args.activeDonationID,
                                    true
                                )
                                binding.progressBar4.visibility = View.GONE
                            }
                        }
                    }
                }
            }

            errorCallback = ErrorCallback {
                Log.e(TAG, "Camera initialization error: ${it.message}")
            }

            binding.scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Log.i("Permission: ", "Granted")
            } else {
                Log.i("Permission: ", "Denied")
            }
        }

    private fun onRequestPermission(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                layout.showSnackBar(
                    view,
                    "Please scan QR code to authenticate the appointment.",
                    Snackbar.LENGTH_LONG,
                    null,
                    requireContext()
                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireContext() as Activity,
                Manifest.permission.CAMERA
            ) -> {
                layout.showSnackBar(
                    view,
                    "Permission is granted. You would use the camera now.",
                    Snackbar.LENGTH_INDEFINITE,
                    "OK",
                    requireContext()
                ) {
                    requestPermissionLauncher.launch(
                        Manifest.permission.CAMERA
                    )
                }
            }

            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.CAMERA
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}