package com.simplyfire.komoottesttask.core.domain

import com.simplyfire.komoottesttask.core.data.PhotoRepository
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.entity.Photos
import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import io.reactivex.Observable
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class SearchPhotoTest {
    private val lattitude = "123"
    private val longitude = "1234"

    private val photoRepository = mock(PhotoRepository::class.java)
    private val searchPhoto = SearchPhoto(photoRepository)

    @Test
    fun `Emit first photo when repository respond with non-empty list`() {
       //arrange
       `when`(photoRepository.searchPhotos(lattitude, longitude)).thenReturn(
           Observable.just(SearchPhotosResponse(
               photos = Photos(photo = listOf(
                   Photo(pkeyId = 1, url_c = "url1"),
                   Photo(pkeyId = 2, url_c = "url2")
               ))
           ))
       )

       //act
        val result = searchPhoto.execute(lattitude, longitude)

        //assert
        result.test().assertResult(Photo(pkeyId = 1, url_c = "url1"))
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