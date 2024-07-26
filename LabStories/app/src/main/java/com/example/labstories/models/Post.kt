package com.example.labstories.models

import com.google.firebase.firestore.PropertyName

data class Post(
    var title:String = "",
    var description:String = "",
    var creation_time_ms: Long = 0,
    var user: User? = null
)