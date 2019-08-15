package com.simplyfire.komoottesttask

import com.simplyfire.komoottesttask.entity.SearchPhotosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


private const val API_KEY = "1dd80d460f02b16a80eaa28eecee5e7c"
private const val SECRET_KEY = "08530f4c468e2d97"

const val API_BASE_URL = "https://www.flickr.com/services/rest/"
private const val JSON_FORMAT_QUERY = "format=json&nojsoncallback=1"
private const val GET_PHOTO_METHOD_QUERY = "method=flickr.photos.search"

interface FlickrApi {

    @GET("?$GET_PHOTO_METHOD_QUERY&api_key=$API_KEY&$JSON_FORMAT_QUERY&extras=url_c")
    fun getPhotosForLocation(@Query("lat")latitude: String, @Query("lon")longitude: String)
    : Call<SearchPhotosResponse>
}