package com.simplyfire.komoottesttask.core.domain

import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.entity.Photos
import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.feature.locationtracking.SearchPhotoInteractor
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SearchPhotoInteractorTest {
    private val lattitude = "123"
    private val longitude = "1234"

    private val photoRepository = mock(PhotoRepository::class.java)
    private val searchPhoto =
        SearchPhotoInteractor(photoRepository)

    @Test
    fun `Emit first photo when repository respond with non-empty list`() {
       //arrange
        val photo = Photo(pkeyId = 1, url_c = "url1")
        `when`(photoRepository.searchPhotos(lattitude, longitude)).thenReturn(
           Observable.just(SearchPhotosResponse(
               photos = Photos(photo = listOf(
                   photo,
                   Photo(pkeyId = 2, url_c = "url2")
               ))
           ))
       )

        `when`(photoRepository.insertPhoto(photo)).thenReturn(
            Single.just(photo)
        )

       //act
        val result = searchPhoto.execute(lattitude, longitude)

        //assert
        result.test().assertResult(photo)
    }

    @Test
    fun `Don't emit anything when repository respond with empty list`() {
        //arrange
        `when`(photoRepository.searchPhotos(lattitude, longitude)).thenReturn(
            Observable.just(SearchPhotosResponse(
                photos = Photos(photo = emptyList())
            ))
        )

        //act
        val result = searchPhoto.execute(lattitude, longitude)

        //assert
        result.test().assertNoValues()
    }
}