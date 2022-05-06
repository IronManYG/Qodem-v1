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
import com.example.qodem.databinding.FragmentEditNameBinding
import com.example.qodem.utils.showSnackbar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditNameFragment : Fragment() {

    companion object {
        const val TAG = "EditNameFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: FragmentEditNameBinding

    private var valuesValidToSignUp by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEditNameBinding.inflate(layoutInflater)

        // Clear the error once more than x (textLength) characters are typed.
        binding.editTextFirstNameField.setOnKeyListener { _, _, _ ->
            // Clear the error.
            binding.editTextFirstNameField.error = null
            false
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.editTextLastNameField.setOnKeyListener { _, _, _ ->
            // Clear the error.
            binding.editTextLastNameField.error = null
            false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfo.collect { userInfo ->
                    binding.apply {
                        editTextFirstNameField.setText(userInfo.firstName)
                        editTextLastNameField.setText(userInfo.lastName)
                    }

                    binding.buttonSave.setOnClickListener {
                        // Check Error State of first name field
                        changeErrorStateInEditTextView(
                            binding.editTextFirstNameField,
                            3,
                            "Minimum Character are 3"
                        )
                        // Check Error State of last name field
                        changeErrorStateInEditTextView(
                            binding.editTextLastNameField,
                            3,
                            "Minimum Character are 3"
                        )

                        // Check Value of name fields
                        valuesValidToSignUp = isAllEditTextValueValid()

                        // Update value if all value are valid
                        if (valuesValidToSignUp) {
                            CoroutineScope(Dispatchers.Main).launch {
                                withContext(Dispatchers.IO) {
                                    viewModel.updateUserName(
                                        userInfo.id,
                                        binding.editTextFirstNameField.text.toString(),
                                        binding.editTextLastNameField.text.toString(),
                                        userInfo.phoneNumber
                                    )
                                }
                            }
                            viewModel.userInfoUpdated.observe(viewLifecycleOwner) {
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
                        } else {
                            binding.root.showSnackbar(
                                binding.root,
                                "Please enter your name",
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
        return isEditTextValueValid(binding.editTextFirstNameField.text, 3) &&
                isEditTextValueValid(binding.editTextLastNameField.text, 3)
    }

}