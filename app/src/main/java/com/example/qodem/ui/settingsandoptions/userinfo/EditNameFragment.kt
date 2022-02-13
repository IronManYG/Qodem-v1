package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.qodem.R
import com.example.qodem.databinding.FragmentEditNameBinding
import com.example.qodem.databinding.UserInfoFragmentBinding
import com.example.qodem.ui.appointment.AppointmentDateFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditNameFragment : Fragment() {

    companion object {
        const val TAG = "EditNameFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentEditNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditNameBinding.inflate(layoutInflater)

        viewModel.userInfo.observe(viewLifecycleOwner){ userInfo ->
            binding.apply {
                editTextFirstNameField.setText(userInfo.firstName)
                editTextLastNameField.setText(userInfo.lastName)
            }

            binding.buttonSave.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        viewModel.updateUserName(
                            userInfo.id,
                            binding.editTextFirstNameField.text.toString(),
                            binding.editTextLastNameField.text.toString(),
                            userInfo.phoneNumber
                        )
                    }
                    viewModel.userInfoUpdated.observe(viewLifecycleOwner){
                        when (it) {
                            true -> {
                                findNavController().popBackStack()
                            }
                            false -> {
                                Toast.makeText(requireActivity(), viewModel.errorResultMessage.value, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }

        return binding.root
    }

}