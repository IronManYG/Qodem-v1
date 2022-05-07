package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.qodem.databinding.FragmentEditDateOfBirthBinding
import com.example.qodem.ui.signup.SignUpActivity
import com.example.qodem.utils.showSnackbar
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditDateOfBirthFragment : Fragment() {

    companion object {
        const val TAG = "EditDateOfBirthFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentEditDateOfBirthBinding

    private var valueValidToSignUp by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditDateOfBirthBinding.inflate(layoutInflater)

        val datePicker = MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Enter Date")
                .build()

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            calendar.add(Calendar.MONTH, 1)
            val birthDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                    "${calendar.get(Calendar.MONTH)}/" +
                    "${calendar.get(Calendar.YEAR)}"
            binding.editTextDateOfBirthField.setText(birthDate)
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.textDateOfBirthLabel.setStartIconOnClickListener {
            // Show date picker in calendar
            datePicker.show(parentFragmentManager, SignUpActivity.TAG)
            // Clear the error.
            binding.editTextDateOfBirthField.error = null
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfo.collect { userInfo ->
                    binding.editTextDateOfBirthField.setText(userInfo.birthDate)

                    binding.buttonSave.setOnClickListener {
                        // Check Error State of date field
                        changeErrorStateInEditTextView(
                            binding.editTextDateOfBirthField,
                            6,
                            "Enter your birth date"
                        )

                        // Check Value of date field
                        valueValidToSignUp = isAllEditTextValueValid()

                        // Update value if all value are valid
                        if (valueValidToSignUp) {
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.updateUserDateOFBirth(
                                        userInfo.id,
                                        binding.editTextDateOfBirthField.text.toString(),
                                        userInfo.phoneNumber
                                    )
                                }
                            }
                            viewLifecycleOwner.lifecycleScope.launch {
                                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                    viewModel.userInfoUpdated.collect() {
                                        when (it) {
                                            true -> {
                                                findNavController().popBackStack()
                                                binding.root.showSnackbar(
                                                    binding.root,
                                                    "Successfully updated",
                                                    Snackbar.LENGTH_SHORT,
                                                    null,
                                                    requireContext()
                                                ) {}
                                            }
                                            false -> {
                                                binding.root.showSnackbar(
                                                    binding.root,
                                                    viewModel.errorResultMessage.value.toString(),
                                                    Snackbar.LENGTH_SHORT,
                                                    null,
                                                    requireContext()
                                                ) {}
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            binding.root.showSnackbar(
                                binding.root,
                                "Please enter your birth date",
                                Snackbar.LENGTH_LONG,
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

    private fun isAllEditTextValueValid(): Boolean {
        return isEditTextValueValid(binding.editTextDateOfBirthField.text, 6)
    }
}