package com.example.qodem.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.qodem.R
import com.example.qodem.databinding.FragmentHomeBinding
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var _viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_home, container, false
        )

        binding.signOutButton.setOnClickListener{ onClick(binding.signOutButton) }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        _viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun onClick(v: View) {
        if (v.id == R.id.sign_out_button) {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(requireContext(), AuthenticationActivity::class.java))
                    activity?.finish()
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }

}