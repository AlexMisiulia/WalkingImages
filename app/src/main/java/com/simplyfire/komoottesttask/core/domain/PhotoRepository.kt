package com.simplyfire.komoottesttask.core.domain

import com.simplyfire.komoottesttask.core.data.db.PhotosDao
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.core.data.network.FlickrApi
import com.simplyfire.komoottesttask.core.utils.SchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val flickrApi: FlickrApi,
                                          private val photosDao: PhotosDao,
                                          private val schedulerProvider: SchedulerProvider) {

    fun searchPhotos(latitude: String, longitude: String): Observable<SearchPhotosResponse> {
        return flickrApi.searchPhotos(latitude, longitude)
            .subscribeOn(schedulerProvider.io())
    }

    fun getPhotos(): Observable<List<Photo>> {
        return photosDao.getAllPhotos()
            .subscribeOn(schedulerProvider.io())
    }

    fun insertPhoto(photo: Photo): Single<Photo> {
        return photosDao.insertAndGet(photo)
            .subscribeOn(schedulerProvider.io())
    }
}