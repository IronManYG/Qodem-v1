package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.qodem.databinding.UserInfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserInfoFragment : Fragment() {

    companion object {
        const val TAG = "UserInfoFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: UserInfoFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = UserInfoFragmentBinding.inflate(layoutInflater)

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            binding.textFullNameField.text = "${userInfo.firstName} " + userInfo.lastName
            binding.textDateOfBirthField.text = userInfo.birthDate
            binding.textBloodTypeField.text = userInfo.bloodType
            binding.textGenderField.text = userInfo.gender
            binding.textCityField.text = userInfo.city
            binding.textIdType.text = userInfo.IDType
            binding.textIdNumber.text = userInfo.IDNumber
        }

        binding.cardFullName.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditNameFragment()
            findNavController().navigate(action)
        }

        binding.cardDateOfBirth.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditDateOfBirthFragment()
            findNavController().navigate(action)
        }

        binding.cardBloodType.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditBloodTypeFragment()
            findNavController().navigate(action)
        }

        binding.cardGender.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditGenderFragment()
            findNavController().navigate(action)
        }

        binding.cardCity.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditCityFragment()
            findNavController().navigate(action)
        }

        binding.cardId.setOnClickListener {
            val action = UserInfoFragmentDirections.actionUserInfoFragmentToEditIdFragment()
            findNavController().navigate(action)
        }

        return binding.root
    }

}