package com.simplyfire.komoottesttask.core.entity

data class Photos(
    val page: Int = 0,
    val pages: String = "",
    val perpage: String = "",
    val photo: List<Photo> = listOf(),
    val total: String = ""
)