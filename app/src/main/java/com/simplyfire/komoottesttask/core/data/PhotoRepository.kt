package com.simplyfire.komoottesttask.core.data

import com.simplyfire.komoottesttask.core.data.db.PhotosDao
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.core.network.FlickrApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val flickrApi: FlickrApi, private val photosDao: PhotosDao) {

    fun searchPhotos(latitude: String, longitude: String): Observable<SearchPhotosResponse> {
        return flickrApi.searchPhotos(latitude, longitude)
            .subscribeOn(Schedulers.io())
    }

    fun getPhotos(): Observable<List<Photo>> {
        return photosDao.getAllPhotos()
            .subscribeOn(Schedulers.io())
    }

    fun insertPhoto(photo: Photo) = photosDao.insert(photo)
}