package com.simplyfire.komoottesttask.core.domain

import com.simplyfire.komoottesttask.core.data.PhotoRepository
import com.simplyfire.komoottesttask.core.entity.Photo
import io.reactivex.Observable
import javax.inject.Inject

private const val TAG = "SearchPhoto"

class SearchPhoto @Inject constructor(private val photoRepository: PhotoRepository){

    @Suppress("FoldInitializerAndIfToElvis")
    fun execute(latitude: String, longitude: String): Observable<Photo> {
        return photoRepository.searchPhotos(latitude, longitude)
            .flatMap { photoResponse ->
                val closestPhoto = photoResponse.photos.photo.getOrNull(0)
                if (closestPhoto == null) return@flatMap Observable.empty<Photo>()

                photoRepository.insertPhoto(closestPhoto)
                return@flatMap Observable.just(closestPhoto)
            }
    }
}