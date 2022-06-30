package com.example.qodem.ui.appointment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.example.qodem.R
import com.example.qodem.databinding.FragmentAuthenticationAppointmentBinding
import com.example.qodem.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AuthenticationAppointmentFragment : Fragment(R.layout.fragment_authentication_appointment) {

    companion object {
        const val TAG = "authAppointmentFrag"
    }

    private val viewModel: AuthenticationAppointmentViewModel by viewModels()

    private lateinit var binding: FragmentAuthenticationAppointmentBinding

    private lateinit var codeScanner: CodeScanner

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentAuthenticationAppointmentBinding.bind(view)

        onRequestPermission()
        codeScanner()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.donationUpdatedState.collect { UpdatedState ->
                    when (UpdatedState) {
                        true -> {
                            findNavController().navigate(AuthenticationAppointmentFragmentDirections.actionToHomeFragment())
                            viewModel.resetDonationUpdatedState()
                            binding.progressBar4.isVisible = false
                        }
                        false -> {
                            if (viewModel.updateErrorMessage.value != "") {
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
        }

    }

    private fun codeScanner() {
        binding.apply {
            codeScanner = CodeScanner(requireContext(), scannerView)

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
                            binding.progressBar4.isVisible = true
                            viewModel.updateDonationAuthenticatedState()
                        }
                    }
                }

                errorCallback = ErrorCallback {
                    Log.e(TAG, "Camera initialization error: ${it.message}")
                }

                scannerView.setOnClickListener {
                    codeScanner.startPreview()
                }
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

    private fun onRequestPermission() {
        binding.apply {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED -> {
                    root.showSnackBar(
                        root,
                        getString(R.string.scan_authenticate_qr),
                        Snackbar.LENGTH_LONG,
                        null,
                        requireContext()
                    ) {}
                }

                shouldShowRequestPermissionRationale(
                    Manifest.permission.CAMERA
                ) -> {
                    root.showSnackBar(
                        root,
                        getString(R.string.camera_permission_granted_message),
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