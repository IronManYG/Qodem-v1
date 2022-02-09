package com.example.qodem.ui.appointment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qodem.R
import com.example.qodem.databinding.FragmentPreScreeningQuestionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.properties.Delegates

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PreScreeningQuestionFragment : Fragment() {

    companion object {
        const val TAG = "PreScreeningQuestion"
    }

    private lateinit var binding: FragmentPreScreeningQuestionBinding

    private var answersValidToBooking by Delegates.notNull<Boolean>()

    private var numberOFAnsweredQuestions by Delegates.notNull<Int>()

    private val args: PreScreeningQuestionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentPreScreeningQuestionBinding.inflate(layoutInflater)

        // initialized it with 0 value every time fragment created
        numberOFAnsweredQuestions = 0

        //
        checkBoxYesNoToggles()

        binding.buttonBookAnAppointment.setOnClickListener{
            // Check Values of all answers
            answersValidToBooking = isAllAnswersValueValid()

            Log.d(TAG, "answered question are : $numberOFAnsweredQuestions")

            if(numberOFAnsweredQuestions == 7){
                if(answersValidToBooking){
                    if(args.bloodBankID == -1){
                        val action = PreScreeningQuestionFragmentDirections.actionPreScreeningQuestionToAppointmentLocationFragment()
                        findNavController().navigate(action)
                    } else {
                        val amount = args.bloodBankID
                        val action = PreScreeningQuestionFragmentDirections.actionPreScreeningQuestionToAppointmentDataFragment(amount)
                        findNavController().navigate(action)
                    }
                } else {
                    findNavController().navigate(R.id.action_preScreeningQuestion_to_homeFragment)
                }
            } else {
                Toast.makeText(requireContext(), "All questions are required to be answered", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    private fun checkBoxYesNoToggles() {
        // for question 1
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion1Yes,binding.checkBoxPreScreeningQusetion1No)
        // for question 2
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion2Yes,binding.checkBoxPreScreeningQusetion2No)
        // for question 3
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion3Yes,binding.checkBoxPreScreeningQusetion3No)
        // for question 4
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion4Yes,binding.checkBoxPreScreeningQusetion4No)
        // for question 5
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion5Yes,binding.checkBoxPreScreeningQusetion5No)
        // for question 6
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion6Yes,binding.checkBoxPreScreeningQusetion6No)
        // for question 7
        checkBoxYesNoToggle(binding.checkBoxPreScreeningQusetion7Yes,binding.checkBoxPreScreeningQusetion7No)
    }

    private fun checkBoxYesNoToggle(yesCheckBox: CheckBox, noCheckBox: CheckBox){
        yesCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                noCheckBox.isChecked = false
                numberOFAnsweredQuestions += 1
            } else {
                numberOFAnsweredQuestions -= 1
            }
        }
        noCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                yesCheckBox.isChecked = false
                numberOFAnsweredQuestions += 1
            } else {
                numberOFAnsweredQuestions -= 1
            }
        }
    }

    private fun isAllAnswersValueValid(): Boolean {
        return binding.checkBoxPreScreeningQusetion1No.isChecked &&
                binding.checkBoxPreScreeningQusetion2No.isChecked &&
                binding.checkBoxPreScreeningQusetion3No.isChecked &&
                binding.checkBoxPreScreeningQusetion4No.isChecked &&
                binding.checkBoxPreScreeningQusetion5No.isChecked &&
                binding.checkBoxPreScreeningQusetion6No.isChecked &&
                binding.checkBoxPreScreeningQusetion7No.isChecked
    }
}