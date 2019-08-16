package com.simplyfire.komoottesttask.core.data

import androidx.lifecycle.LiveData
import com.simplyfire.komoottesttask.core.data.db.PhotosDao
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.network.FlickrApi
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoRepository @Inject constructor(private val flickrApi: FlickrApi, private val photosDao: PhotosDao) {

    fun searchPhoto(latitude: String, longitude: String) {
        flickrApi.searchPhotos(latitude, longitude)
            .subscribeOn(Schedulers.io())
            .subscribe { photoResponse ->
                val closestPhoto = photoResponse.photos.photo.getOrNull(0)
                if (closestPhoto != null) photosDao.insert(closestPhoto)
            }
    }

    fun getPhotos(): LiveData<List<Photo>> {
        return photosDao.getAllPhotos()
    }
}