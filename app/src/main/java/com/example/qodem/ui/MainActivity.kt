package com.example.qodem.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.qodem.R
import com.example.qodem.databinding.ActivityMainBinding
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var toggle : ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val navController = findNavController(R.id.fragmentContainerView)

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
            when(it.itemId){
              R.id.signOut -> onClick(navView.findViewById(R.id.signOut))
            }
            true
        }

    }

    private fun bottomNavigationView(navController: NavController){
        val bottomNavigationView = binding.bottomNavigationView

        bottomNavigationView.setupWithNavController(navController)
    }

    private fun onClick(v: View) {
        if (v.id == R.id.signOut) {
            AuthUI.getInstance()
                .signOut(applicationContext)
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(applicationContext, AuthenticationActivity::class.java))
                    finish()
                }
        }
    }
}