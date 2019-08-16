package com.simplyfire.komoottesttask.core.data

import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.core.network.FlickrApi
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val flickrApi: FlickrApi) {

    fun searchPhotos(latitude: String, longitude: String) : Observable<SearchPhotosResponse> {
        return flickrApi.searchPhotos(latitude, longitude)
    }

    fun observePhotos() {
        // TODO add Room database
        return
    }
}