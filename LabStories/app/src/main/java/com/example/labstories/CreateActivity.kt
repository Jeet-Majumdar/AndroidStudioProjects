package com.example.labstories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.labstories.models.Post
import com.example.labstories.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firestore.v1beta1.DocumentTransform
import kotlinx.android.synthetic.main.activity_create.*
import java.time.Instant

private const val TAG = "CreateActivity"

class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        btnSubmit.setOnClickListener {
            btnSubmit.isEnabled = false
            saveData()
            btnSubmit.isEnabled = true
        }


    }


    private fun saveData() {
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Title/Description cannot be empty!", Toast.LENGTH_SHORT)
                .show()
            return
        }
       // Log.i(TAG, "Successful till here")

        val signedInUsername: String = intent.getStringExtra("EXTRA_USER_NAME") ?: ""
        val signedInAadhaar: Long = intent.getLongExtra("EXTRA_USER_AADHAAR", -1)


        if(signedInUsername.isBlank()){
            Log.e(TAG, "Illegal Post!")
            return
        }
        Log.i(TAG, "Successful getting the username: ${signedInUsername}")


        val dbCreateRef = FirebaseFirestore.getInstance()
        val time: Long = DocumentTransform.FieldTransform.ServerValue.REQUEST_TIME_VALUE.toLong()
            dbCreateRef.collection("posts")
            .add(Post(title = title,
                description = description,
                creation_time_ms = time,
                user = User(username = signedInUsername, aadhaar = signedInAadhaar)))
            .addOnSuccessListener {
                Toast.makeText(this, "Posted Successfully!", Toast.LENGTH_SHORT)
                    .show()
                Log.i(TAG, "Successfully Added User Data with username: ${signedInUsername}")
                etTitle.text.clear()
                etDescription.text.clear()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Post Error!", Toast.LENGTH_SHORT)
                    .show()
                Log.e(TAG, "Post error with username: ${signedInUsername}")
            }


        return
    }
}
