package com.example.qodem.ui.appointment

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.qodem.R
import com.example.qodem.databinding.FragmentPreScreeningQuestionBinding
import com.example.qodem.utils.showSnackBar
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PreScreeningQuestionFragment : Fragment(R.layout.fragment_pre_screening_question) {

    private lateinit var binding: FragmentPreScreeningQuestionBinding

    private var answersValidToBooking = false

    private var numberOFAnsweredQuestions = 0

    private val args: PreScreeningQuestionFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inflate the layout for this fragment
        binding = FragmentPreScreeningQuestionBinding.bind(view)

        checkBoxYesNoToggles()

        binding.apply {

            buttonBookAnAppointment.setOnClickListener {
                // Check Values of all answers
                answersValidToBooking = isAllAnswersValueValid()

                if (numberOFAnsweredQuestions == 7) {
                    if (answersValidToBooking) {
                        if (args.selectedBloodBank == null) {
                            val action =
                                PreScreeningQuestionFragmentDirections.actionPreScreeningQuestionToAppointmentLocationFragment()
                            findNavController().navigate(action)
                        } else {
                            val action =
                                PreScreeningQuestionFragmentDirections.actionPreScreeningQuestionToAppointmentDataFragment(
                                    args.bloodBankID,
                                    args.selectedBloodBank
                                )
                            findNavController().navigate(action)
                        }
                    } else {
                        findNavController().navigate(R.id.action_preScreeningQuestion_to_homeFragment)
                        root.showSnackBar(
                            root,
                            getString(R.string.not_eligible_to_donate_message),
                            Snackbar.LENGTH_LONG,
                            null,
                            requireContext()
                        ) {}
                    }
                } else {
                    root.showSnackBar(
                        root,
                        getString(R.string.answere_all_questions),
                        Snackbar.LENGTH_LONG,
                        null,
                        requireContext()
                    ) {}
                }
            }

        }

    }

    private fun checkBoxYesNoToggles() {
        binding.apply {
            // for question 1
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion1Yes,
                checkBoxPreScreeningQuestion1No
            )
            // for question 2
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion2Yes,
                checkBoxPreScreeningQuestion2No
            )
            // for question 3
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion3Yes,
                checkBoxPreScreeningQuestion3No
            )
            // for question 4
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion4Yes,
                checkBoxPreScreeningQuestion4No
            )
            // for question 5
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion5Yes,
                checkBoxPreScreeningQuestion5No
            )
            // for question 6
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion6Yes,
                checkBoxPreScreeningQuestion6No
            )
            // for question 7
            checkBoxYesNoToggle(
                checkBoxPreScreeningQuestion7Yes,
                checkBoxPreScreeningQuestion7No
            )
        }
    }

    private fun checkBoxYesNoToggle(yesCheckBox: CheckBox, noCheckBox: CheckBox) {
        yesCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                noCheckBox.isChecked = false
                numberOFAnsweredQuestions += 1
            } else {
                numberOFAnsweredQuestions -= 1
            }
        }
        noCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                yesCheckBox.isChecked = false
                numberOFAnsweredQuestions += 1
            } else {
                numberOFAnsweredQuestions -= 1
            }
        }
    }

    private fun isAllAnswersValueValid(): Boolean {
        return binding.checkBoxPreScreeningQuestion1No.isChecked &&
                binding.checkBoxPreScreeningQuestion2No.isChecked &&
                binding.checkBoxPreScreeningQuestion3No.isChecked &&
                binding.checkBoxPreScreeningQuestion4No.isChecked &&
                binding.checkBoxPreScreeningQuestion5No.isChecked &&
                binding.checkBoxPreScreeningQuestion6No.isChecked &&
                binding.checkBoxPreScreeningQuestion7No.isChecked
    }
}