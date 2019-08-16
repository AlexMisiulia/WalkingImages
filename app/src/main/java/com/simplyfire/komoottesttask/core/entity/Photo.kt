package com.simplyfire.komoottesttask.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    // so lazy to create several tables with relationships
    // for cases when user return to the same place during the walk and receive the same photo once again
    // just create increment pkeyId for now
    @PrimaryKey(autoGenerate = true)
    val pkeyId: Int = 0,

    val id: String = "",
    val farm: Int = 0,
    val isfamily: Int = 0,
    val isfriend: Int = 0,
    val ispublic: Int = 0,
    val owner: String = "",
    val secret: String = "",
    val server: String = "",
    val title: String = "",
    val url_c: String = ""
)