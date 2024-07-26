package com.example.labstories

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.IntentCompat
import com.google.firebase.auth.FirebaseAuth


private const val TAG = "ProfileActivity"

class ProfileActivity : PostsActivity() {

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_profile, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_logout){
            Log.i(TAG, "User wants to Logout!")
            FirebaseAuth.getInstance().signOut()

            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)

            //startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        return super.onContextItemSelected(item)
    }


}
