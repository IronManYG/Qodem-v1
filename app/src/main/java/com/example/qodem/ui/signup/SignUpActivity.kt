package com.example.qodem.ui.signup

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.annotation.RequiresApi
import com.example.qodem.R
import com.example.qodem.databinding.ActivitySignUpBinding
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var binding: ActivitySignUpBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setInputMode(MaterialDatePicker.INPUT_MODE_TEXT)
                .setTitleText("Enter Date")
                .build()

        binding.editTextDataOfBirthField.setOnClickListener {
            datePicker.show(supportFragmentManager, TAG)
        }

        datePicker.addOnPositiveButtonClickListener {
           binding.editTextDataOfBirthField.setText(datePicker.headerText)
        }

        binding.menuIdType.setOnClickListener {

//            val selectedValue: String = (it.getEditText() as AutoCompleteTextView).text


            Log.d("here here", it.autofillValue.toString())

        }



        val bloodTypeItems = listOf("O+", "O-", "A+", "A-","B+", "B-", "AB+", "AB-")
        val bloodTypeAdapter = ArrayAdapter(applicationContext, R.layout.list_item, bloodTypeItems)
        (binding.menuBloodType.editText as? AutoCompleteTextView)?.setAdapter(bloodTypeAdapter)

        val genderItems = listOf("Male", "Female")
        val genderAdapter = ArrayAdapter(applicationContext, R.layout.list_item, genderItems)
        (binding.menuGender.editText as? AutoCompleteTextView)?.setAdapter(genderAdapter)

        val cityItems = listOf("Khartoum", "Khartoum North","Omdurman")
        val cityAdapter = ArrayAdapter(applicationContext, R.layout.list_item, cityItems)
        (binding.menuCity.editText as? AutoCompleteTextView)?.setAdapter(cityAdapter)

        val idItems = listOf("Passport", "National ID")
        val idAdapter = ArrayAdapter(applicationContext, R.layout.list_item, idItems)
        (binding.menuIdType.editText as? AutoCompleteTextView)?.setAdapter(idAdapter)
    }

}