package com.evans.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reg_btn.setOnClickListener {
            register()
        }

        reg_already_have_an_account.setOnClickListener {
            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun register() {
        val email = reg_email.text.toString()
        val password = reg_password.text.toString()

        if (email.isNotEmpty()) {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.isNotEmpty()) {
                    if (password.length >= 6){
                        doRegister(email, password)
                    } else{
                        reg_password.error = "Password should be at least 6 characters"
                        reg_password.requestFocus()
                    }
                } else {
                    reg_password.error = "Password is required"
                    reg_password.requestFocus()
                }
            } else {
                reg_email.error = "Enter a valid email"
                reg_email.requestFocus()
            }
        } else {
            reg_email.error = "Email is required"
            reg_email.requestFocus()
        }
    }

    private fun doRegister(email: String, password: String) {
        //perform firebase authentication to create user with email and password
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                // else if request is successful
                Log.d("MainActivity", "User created with uid: ${it.result!!.user.uid}")
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
