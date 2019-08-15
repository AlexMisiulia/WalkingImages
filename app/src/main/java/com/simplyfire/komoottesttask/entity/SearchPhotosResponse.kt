package com.simplyfire.komoottesttask.entity

data class SearchPhotosResponse(
    val photos: Photos = Photos(),
    val stat: String = ""
)