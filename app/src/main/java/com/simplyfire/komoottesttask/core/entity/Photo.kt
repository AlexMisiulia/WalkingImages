package com.simplyfire.komoottesttask.core.entity

data class Photo(
    val farm: Int = 0,
    val id: String = "",
    val isfamily: Int = 0,
    val isfriend: Int = 0,
    val ispublic: Int = 0,
    val owner: String = "",
    val secret: String = "",
    val server: String = "",
    val title: String = "",
    val url_c: String = ""
)