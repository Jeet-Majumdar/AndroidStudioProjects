package com.example.labstories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

private const val TAG = "LoginActivity"

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener {
            btnLogin.isEnabled = false
            val email = ptEmail.text.toString()
            val password = pPassword.text.toString()
            if(email.isBlank() || password.isBlank()) {
                btnLogin.isEnabled = true
                Toast.makeText(this, "Email/password cannot be empty!", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            // Firebase Authentication check
            val auth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show()
                        goPostActivity()
                    } else {
                        Log.i(TAG, "signInWithEmail failed!", task.exception)
                        btnLogin.isEnabled = true
                        Toast.makeText(this, "Authentication Failed!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }

    private fun goPostActivity() {
        Log.i(TAG, "goPostsActivity")
        val intent = Intent(this, PostsActivity::class.java)
        startActivity(intent)
        finish()
    }

}
