package com.example.instafire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.instafire.models.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

private const val TAG = "PostsActivity"

open class PostsActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)




        firestoreDb = FirebaseFirestore.getInstance()
        val postReference = firestoreDb.collection("posts")
            .limit(20)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)
        postReference.addSnapshotListener{ snapshot, exception ->
            if(exception != null || snapshot == null){
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)

            for (post in postList){
                Log.i(TAG, "Post ${post}", exception)
            }


        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_profile){
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
