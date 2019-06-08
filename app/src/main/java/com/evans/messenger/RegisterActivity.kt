package com.evans.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        reg_btn.setOnClickListener {
            register()
        }
        reg_already_have_an_account.setOnClickListener {
            //launch login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        reg_image.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            //proceed to check what the selected image was
            selectedPhotoUri = data.data
            reg_image.setImageURI(selectedPhotoUri)//show image in the view
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
                Log.d("Register", "User created with uid: ${it.result!!.user.uid}")
                uploadImage()
            }
            .addOnFailureListener{
                Toast.makeText(this, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun uploadImage() {
        if (selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("Register", "Successfully uploaded image: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    saveUser(it.toString())//save user to database with the selected image
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUser(profileImage: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""//check if uid is null
        val ref = FirebaseDatabase.getInstance().getReference("users/$uid")

        val user = User(uid, reg_username.text.toString(), profileImage)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "User saved")
            }
    }
}

class User(val uid: String, val username: String, val profileImage: String)
