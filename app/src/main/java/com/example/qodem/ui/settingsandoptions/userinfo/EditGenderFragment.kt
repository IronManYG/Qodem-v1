package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.qodem.R
import com.example.qodem.databinding.FragmentEditDateOfBirthBinding
import com.example.qodem.databinding.FragmentEditGenderBinding
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditGenderFragment : Fragment() {

    companion object {
        const val TAG = "EditGenderFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentEditGenderBinding

    private var valueValidToSignUp by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditGenderBinding.inflate(layoutInflater)

        val genderItems = resources.getStringArray(R.array.genders)

        setItemsToExposedDropdownMenu(genderItems, binding.menuGender)

        // Clear the error once more than x (textLength) characters are typed.
        binding.menuGenderField.setOnDismissListener {
            // Clear the error.
            binding.menuGender.editText?.error = null
        }

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->

            binding.buttonSave.setOnClickListener {
                // Check Error State of date field
                changeErrorStateInEditTextView(
                    binding.menuGender.editText!!,
                    1,
                    "Enter Your Gender"
                )

                // Check Value of gender field
                valueValidToSignUp = isEditTextValueValid()

                // Update value if all value are valid
                if (valueValidToSignUp) {
                    CoroutineScope(Dispatchers.Main).launch {
                        withContext(Dispatchers.IO) {
                            viewModel.updateUserGender(
                                userInfo.id,
                                binding.menuGenderField.text.toString(),
                                userInfo.phoneNumber
                            )
                        }
                    }
                    viewModel.userInfoUpdated.observe(viewLifecycleOwner) {
                        when (it) {
                            true -> {
                                findNavController().popBackStack()
                            }
                            false -> {
                                Toast.makeText(
                                    requireActivity(),
                                    viewModel.errorResultMessage.value,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Please enter your Gender",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun setItemsToExposedDropdownMenu(items: Array<String>, layout: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        (layout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun isEditTextValueValid(text: Editable?, textLength: Int): Boolean {
        return text != null && text.length >= textLength
    }

    private fun changeErrorStateInEditTextView(
        editText: EditText,
        textLength: Int,
        errorMassage: String,
    ) {
        // Set an error if the EditTextValue is less than x (textLength) characters.
        if (!isEditTextValueValid(editText.text, textLength)) {
            editText.error = errorMassage
        } else {
            // Clear the error.
            editText.error = null
        }
    }

    private fun isEditTextValueValid(): Boolean {
        return isEditTextValueValid(binding.menuGender.editText!!.text, 1)
    }
}