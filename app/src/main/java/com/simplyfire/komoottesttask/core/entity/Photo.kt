package com.simplyfire.komoottesttask.core.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey
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