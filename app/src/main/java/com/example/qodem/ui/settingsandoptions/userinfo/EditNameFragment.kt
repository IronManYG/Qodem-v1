package com.example.qodem.ui.settingsandoptions.userinfo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.qodem.R
import com.example.qodem.databinding.UserInfoFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EditNameFragment : Fragment() {

    companion object {
        const val TAG = "EditNameFragment"
    }

    private val viewModel: UserInfoViewModel by viewModels()

    private lateinit var binding: EditNameFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = EditNameFragmentBinding.inflate(layoutInflater)

        return inflater.inflate(R.layout.fragment_edit_name, container, false)
    }

}