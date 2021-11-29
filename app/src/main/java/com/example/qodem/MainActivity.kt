package com.example.qodem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.view.View
import com.example.qodem.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signOutButton = findViewById<Button>(R.id.sign_out_button)
        // : Implement the create account and sign in using FirebaseUI, use sign in using email and sign in using Google
        signOutButton.setOnClickListener { onClick(signOutButton) }
    }

    private fun onClick(v: View) {
        if (v.id == R.id.sign_out_button) {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener { // user is now signed out
                    startActivity(Intent(this@MainActivity, AuthenticationActivity::class.java))
                    finish()
                }
        }
    }
}