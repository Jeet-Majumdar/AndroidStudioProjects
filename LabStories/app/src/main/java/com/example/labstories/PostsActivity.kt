package com.example.labstories

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.labstories.models.Post
import com.example.labstories.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_posts.*

private const val TAG = "PostsActivity"
private const val EXTRA_USERNAME = "EXTRA_USER_NAME"
private const val EXTRA_AADHAAR = "EXTRA_USER_AADHAAR"
open class PostsActivity : AppCompatActivity() {

    private var signedInUser: User?=null
    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var posts: MutableList<Post>
    private lateinit var adapter: PostsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)


        // -- Steps to use a recyclerView:
        // Create the layout file which represent one post [Here: item_post.xml]
        // Create data source
        posts = mutableListOf()
        // Create the adaptor
        adapter = PostsAdapter(this, posts)
        // Bind the adaptor and layout manager to the recyclerView
        rvPosts.adapter = adapter
        rvPosts.layoutManager = LinearLayoutManager(this)

        // Retrive Data from Firebase
        firestoreDb = FirebaseFirestore.getInstance()

        firestoreDb.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid as String)
            .get()
            .addOnSuccessListener { userSnapshot ->
                signedInUser = userSnapshot.toObject(User::class.java)
                Log.i(TAG, "signed in user: ${signedInUser}")
            }
            .addOnFailureListener{exception ->
                Log.e(TAG, "Failure fetching signed in user", exception)
            }

        var postReference = firestoreDb
            .collection("posts")
            .limit(20)
            .orderBy("creation_time_ms", Query.Direction.DESCENDING)

        var username = intent.getStringExtra(EXTRA_USERNAME)
        if(username != null){
            supportActionBar?.title = username
            postReference = postReference.whereEqualTo("user.username", username)
        }

        postReference.addSnapshotListener { snapshot, exception ->
            if (exception != null || snapshot == null) {
                Log.e(TAG, "Exception when querying posts", exception)
                return@addSnapshotListener
            }


            val postList = snapshot.toObjects(Post::class.java)
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()

            for (post in postList) {
                Log.i(TAG, "Post ${post}")
            }

/*
            for (document in snapshot.documents) {
                Log.i(TAG, "Document ${document.id}:  ${document.data}")
            }
*/
        fabCreate.setOnClickListener{
            val intent = Intent(this, CreateActivity::class.java)
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            intent.putExtra(EXTRA_AADHAAR, signedInUser?.aadhaar)
            startActivity(intent)
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
            intent.putExtra(EXTRA_USERNAME, signedInUser?.username)
            intent.putExtra(EXTRA_AADHAAR, signedInUser?.aadhaar)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}