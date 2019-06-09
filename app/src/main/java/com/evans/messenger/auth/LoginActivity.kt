package com.evans.messenger.auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.evans.messenger.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            login()
        }

        login_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun login() {
        val email = login_email.text.toString()
        val password = login_password.text.toString()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {
                doLogin(email, password)
            } else {
                login_password.error  = "Password is required"
                login_password.requestFocus()
            }
        } else {
            login_email.error = "Email is required"
            login_email.requestFocus()
        }
    }

    private fun doLogin(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)

    }
}
