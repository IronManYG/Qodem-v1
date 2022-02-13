package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.qodem.R
import com.example.qodem.databinding.FragmentEditBloodTypeBinding
import com.example.qodem.databinding.FragmentEditNameBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditBloodTypeFragment : Fragment() {

    companion object {
        const val TAG = "EditBloodTypeFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentEditBloodTypeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditBloodTypeBinding.inflate(layoutInflater)

        val bloodTypeItems = resources.getStringArray(R.array.blood_types)

        setItemsToExposedDropdownMenu(bloodTypeItems, binding.menuBloodType)

        viewModel.userInfo.observe(viewLifecycleOwner){ userInfo ->
            binding.menuBloodTypeField.setText(userInfo.bloodType)

            binding.buttonSave.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        viewModel.updateUserBloodType(
                            userInfo.id,
                            binding.menuBloodTypeField.text.toString(),
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

    private fun setItemsToExposedDropdownMenu(items: Array<String>, layout: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (layout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }
}