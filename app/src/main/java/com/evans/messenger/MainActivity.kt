package com.evans.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reg_btn.setOnClickListener {
            val email = reg_email.text.toString()
            val password = reg_password.text.toString()

            Log.d("MainActivity", "Email is : $email")
            Log.d("MainActivity", "Password is : $password")

            //perform firebase authentication to create user with email and password
        }

        reg_already_have_an_account.setOnClickListener {
            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }
}
