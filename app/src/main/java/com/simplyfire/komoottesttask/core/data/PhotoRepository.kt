package com.simplyfire.komoottesttask.core.data

import com.simplyfire.komoottesttask.core.data.db.PhotosDao
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.network.FlickrApi
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val flickrApi: FlickrApi, private val photosDao: PhotosDao) {

    fun searchPhoto(latitude: String, longitude: String): Observable<Photo> {
        return flickrApi.searchPhotos(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .flatMap { photoResponse ->
                val closestPhoto = photoResponse.photos.photo.getOrNull(0)
                if(closestPhoto == null) return@flatMap Observable.empty<Photo>()

                photosDao.insert(closestPhoto)
                return@flatMap Observable.just(closestPhoto)
            }
    }

    fun getPhotos(): Observable<List<Photo>> {
        return photosDao.getAllPhotos()
            .subscribeOn(Schedulers.io())
    }
}