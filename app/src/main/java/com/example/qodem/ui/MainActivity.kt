package com.example.qodem.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.qodem.R
import com.example.qodem.databinding.ActivityMainBinding
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.example.qodem.ui.home.HomeViewModel
import com.firebase.ui.auth.AuthUI
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.fragmentContainerView)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id) {
                R.id.homeFragment, R.id.locationFragment, R.id.communityFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)

                }
            }
        }

        bottomNavigationView(navController)

        setupDrawerView(navController)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupDrawerView(navController: NavController){
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout , R.string.open, R.string.close)

        binding.drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navView = binding.navView

        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener {
            onClick(it.itemId,navController)
            true
        }

        val headerView = navView.getHeaderView(0)
        val userName: TextView = headerView.findViewById(R.id.text_user_name)
        val userImage: ImageView = headerView.findViewById(R.id.image_user)
        viewModel.userInfo.observe(this){
            userName.text = "${it.firstName} ${it.lastName}"
            //userImage.setImageResource(R.drawable.infographic1)
        }

        headerView.setOnClickListener {
            navController.navigate(R.id.userInfoFragment)
            binding.drawerLayout.close()
        }
    }

    private fun bottomNavigationView(navController: NavController){
        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun onClick(viewID: Int, navController: NavController) {
        when (viewID) {
            R.id.homeFragment -> {
                navController.navigate(R.id.homeFragment)
                binding.drawerLayout.close()
            }
            R.id.locationFragment -> {
                navController.navigate(R.id.locationFragment)
                binding.drawerLayout.close()
            }
            R.id.communityFragment -> {
                navController.navigate(R.id.communityFragment)
                binding.drawerLayout.close()
            }
            R.id.signOut -> {
                AuthUI.getInstance()
                    .signOut(applicationContext)
                    .addOnCompleteListener { // user is now signed out
                        startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
                        finish()
                    }
            }
        }
    }
}