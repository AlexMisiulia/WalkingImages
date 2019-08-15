package com.simplyfire.komoottesttask.entity

data class Photos(
    val page: Int = 0,
    val pages: String = "",
    val perpage: String = "",
    val photo: List<Photo> = listOf(),
    val total: String = ""
)