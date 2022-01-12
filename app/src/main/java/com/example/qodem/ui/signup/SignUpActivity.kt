package com.example.qodem.ui.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.qodem.R
import com.example.qodem.data.userinfo.remote.UserNetworkEntity
import com.example.qodem.databinding.ActivitySignUpBinding
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.example.qodem.ui.authentication.AuthenticationViewModel
import com.firebase.ui.auth.AuthUI
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var binding: ActivitySignUpBinding

    private val viewModel: AuthenticationViewModel by viewModels()

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

        setItemsToExposedDropdownMenu(bloodTypeItems,binding.menuBloodType)

        setItemsToExposedDropdownMenu(genderItems,binding.menuGender)

        setItemsToExposedDropdownMenu(cityItems.toTypedArray(),binding.menuCity)

        setItemsToExposedDropdownMenu(idItems,binding.menuIdType)

        binding.textDataOfBirthLabel.setStartIconOnClickListener {
            datePicker.show(supportFragmentManager, TAG)
        }

        datePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(it)
            calendar.add(Calendar.MONTH, 1)
            val birthDate = "${calendar.get(Calendar.DAY_OF_MONTH)}/" +
                    "${calendar.get(Calendar.MONTH)}/" +
                    "${calendar.get(Calendar.YEAR)}"
            binding.editTextDataOfBirthField.setText(birthDate)
        }

        binding.menuIdTypeField.setOnDismissListener {
            binding.menuIdType.editText?.text.isNullOrEmpty().let {
                when (it) {
                    true -> binding.editTextIdNumberField.isEnabled = false
                    false -> binding.editTextIdNumberField.isEnabled = true
                }
            }
        }

        binding.buttonCancelSignUp.setOnClickListener{
            AuthUI.getInstance()
                .signOut(applicationContext)
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
                    finish()
                }
        }

        binding.buttonSignUp.setOnClickListener{
            val userPhone = intent.getStringExtra("USER_PHONENUMBER")

            val userInfo = UserNetworkEntity(
                firstName = binding.textFirstNameLabel.editText?.text.toString(),
                lastName = binding.textLastNameLabel.editText?.text.toString(),
                bloodType = binding.menuBloodType.editText?.text.toString(),
                birthDate = binding.textDataOfBirthLabel.editText?.text.toString(),
                city = binding.menuCity.editText?.text.toString(),
                phoneNumber = userPhone!!,
                IDType = binding.menuIdType.editText?.text.toString(),
                IDNumber = binding.textIdNumberLabel.editText?.text.toString()
            )

            // Set an error if the EditTextValue is less than x (textLength) characters.
            if (!isEditTextValueValid(binding.editTextFirstNameField.text!!,3)){
                binding.editTextFirstNameField.error = "minimum character is 3"
                //binding.textFirstNameLabel.endIconMode = TextInputLayout.END_ICON_CUSTOM
            } else {
                // Clear the error.
                binding.editTextFirstNameField.error = null
                // Sign up user
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.saveUserInfo(userInfo)
                }
            }
        }

        // Clear the error once more than x (textLength) characters are typed.
        binding.editTextFirstNameField.setOnKeyListener { _, _, _ ->
            if (isEditTextValueValid(binding.editTextFirstNameField.text!!,3)) {
                // Clear the error.
                binding.editTextFirstNameField.error = null
            }
            false
        }

    }

    private fun setItemsToExposedDropdownMenu(items: Array<String>, layout: TextInputLayout){
        val adapter = ArrayAdapter(applicationContext, R.layout.list_item, items)
        (layout.editText as? AutoCompleteTextView)?.setAdapter(adapter)
    }

    private fun isEditTextValueValid(text: Editable?, textLength: Int): Boolean {
        return text != null && text.length >= textLength
    }

}