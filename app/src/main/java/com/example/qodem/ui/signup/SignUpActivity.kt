package com.example.qodem.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qodem.R
import com.example.qodem.data.userinfo.remote.UserNetworkEntity
import com.example.qodem.databinding.ActivitySignUpBinding
import com.example.qodem.ui.MainActivity
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.example.qodem.ui.authentication.AuthenticationViewModel
import com.example.qodem.utils.showSnackbar
import com.firebase.ui.auth.AuthUI
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: AuthenticationViewModel by viewModels()

    private var valuesValidToSignUp by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val bloodTypeItems = resources.getStringArray(R.array.blood_types)

        val genderItems = resources.getStringArray(R.array.genders)

        val cityItems = listOf("Khartoum", "Khartoum North", "Omdurman")

        val idItems = resources.getStringArray(R.array.id_types)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Enter Date")
                .build()

        setItemsToExposedDropdownMenu(bloodTypeItems, binding.menuBloodType)

        setItemsToExposedDropdownMenu(genderItems, binding.menuGender)

        setItemsToExposedDropdownMenu(cityItems.toTypedArray(), binding.menuCity)

        setItemsToExposedDropdownMenu(idItems, binding.menuIdType)

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

        // Clear the error once more than x (textLength) characters are typed.
        binding.textDateOfBirthLabel.setStartIconOnClickListener {
            // Show date picker in calendar
            datePicker.show(supportFragmentManager, TAG)
            // Clear the error.
            binding.editTextDateOfBirthField.error = null
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.menuBloodTypeField.setOnDismissListener {
            // Clear the error.
            binding.menuBloodType.editText?.error = null
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.menuGenderField.setOnDismissListener {
            // Clear the error.
            binding.menuGender.editText?.error = null
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.menuCityField.setOnDismissListener {
            // Clear the error.
            binding.menuCity.editText?.error = null
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.menuIdTypeField.setOnDismissListener {
            // Clear the error.
            binding.menuIdType.editText?.error = null
            // Enable edit text in ID number field
            binding.menuIdType.editText?.text.isNullOrEmpty().let {
                when (it) {
                    true -> binding.editTextIdNumberField.isEnabled = false
                    false -> binding.editTextIdNumberField.isEnabled = true
                }
            }
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.editTextIdNumberField.setOnKeyListener { _, _, _ ->
            // Clear the error.
            binding.editTextIdNumberField.error = null
            false
        }

        binding.buttonCancelSignUp.setOnClickListener {
            AuthUI.getInstance()
                .signOut(applicationContext)
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
                    finish()
                }
        }

        binding.buttonSignUp.setOnClickListener {
            val userPhone = intent.getStringExtra("USER_PHONE_NUMBER")
            val userInfo = UserNetworkEntity(
                firstName = binding.textFirstNameLabel.editText?.text.toString(),
                lastName = binding.textLastNameLabel.editText?.text.toString(),
                bloodType = binding.menuBloodType.editText?.text.toString(),
                birthDate = binding.textDateOfBirthLabel.editText?.text.toString(),
                gender = binding.menuGender.editText?.text.toString(),
                city = binding.menuCity.editText?.text.toString(),
                phoneNumber = userPhone!!,
                IDType = binding.menuIdType.editText?.text.toString(),
                IDNumber = binding.textIdNumberLabel.editText?.text.toString()
            )

            // Check Error State of all fields
            changeErrorStateInAllEditTextView()

            // Check Values of all fields
            valuesValidToSignUp = isAllEditTextValueValid()

            // Sign up user if all values are valid
            if (valuesValidToSignUp) {
                // Sign up user
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.Main) {
                        viewModel.saveUserInfo(userInfo)
                        viewModel.userInfoSaveState.observe(this@SignUpActivity) {
                            when (it) {
                                true -> {
                                    val intent = Intent(
                                        this@SignUpActivity,
                                        MainActivity::class.java
                                    )
                                    startActivity(intent)
                                    finish()
                                    Log.e(TAG, "user Saved!")
                                }
                                false -> {
                                    binding.root.showSnackbar(
                                        binding.root,
                                        viewModel.saveErrorMessage.value.toString(),
                                        Snackbar.LENGTH_LONG,
                                        null,
                                        this@SignUpActivity
                                    ) {}
                                    Log.e(TAG, "user not Saved")
                                }
                            }
                        }
                    }
                }
            } else {
                binding.root.showSnackbar(
                    binding.root,
                    "Please enter all info",
                    Snackbar.LENGTH_LONG,
                    null,
                    this@SignUpActivity
                ) {}
            }

        }

    }

    private fun setItemsToExposedDropdownMenu(items: Array<String>, layout: TextInputLayout) {
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
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

    private fun changeErrorStateInAllEditTextView() {
        changeErrorStateInEditTextView(
            binding.editTextFirstNameField,
            3,
            "Minimum Character are 3"
        )
        changeErrorStateInEditTextView(
            binding.editTextLastNameField,
            3,
            "Minimum Character are 3"
        )
        changeErrorStateInEditTextView(
            binding.editTextDateOfBirthField,
            6,
            "Enter your birth date"
        )
        changeErrorStateInEditTextView(
            binding.menuBloodType.editText!!,
            1,
            "Enter your Blood Type"
        )
        changeErrorStateInEditTextView(
            binding.menuGender.editText!!,
            1,
            "Enter Your Gender"
        )
        changeErrorStateInEditTextView(
            binding.menuCity.editText!!,
            1,
            "Enter Your City"
        )
        changeErrorStateInEditTextView(
            binding.menuIdType.editText!!,
            1,
            "Enter Your ID Type"
        )
        changeErrorStateInEditTextView(
            binding.editTextIdNumberField,
            9,
            "Minimum Character are 9"
        )
    }

    private fun isAllEditTextValueValid(): Boolean {
        return isEditTextValueValid(binding.editTextFirstNameField.text, 3) &&
                isEditTextValueValid(binding.editTextLastNameField.text, 3) &&
                isEditTextValueValid(binding.editTextDateOfBirthField.text, 6) &&
                isEditTextValueValid(binding.menuBloodType.editText!!.text, 1) &&
                isEditTextValueValid(binding.menuGender.editText!!.text, 1) &&
                isEditTextValueValid(binding.menuCity.editText!!.text, 1) &&
                isEditTextValueValid(binding.menuIdType.editText!!.text, 1) &&
                isEditTextValueValid(binding.editTextIdNumberField.text, 9)
    }

}