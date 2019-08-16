package com.simplyfire.komoottesttask.core.entity

data class SearchPhotosResponse(
    val photos: Photos = Photos(),
    val stat: String = ""
)