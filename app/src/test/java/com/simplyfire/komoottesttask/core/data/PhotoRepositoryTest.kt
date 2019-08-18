package com.simplyfire.komoottesttask.core.data

import com.simplyfire.komoottesttask.core.data.db.PhotosDao
import com.simplyfire.komoottesttask.core.data.network.FlickrApi
import com.simplyfire.komoottesttask.core.entity.Photo
import com.simplyfire.komoottesttask.core.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.testutils.TestSchedulerProvider
import io.reactivex.Observable
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito.*

class PhotoRepositoryTest {
    private val latitude = "123"
    private val longitude = "124"

    private val flickrApi = mock(FlickrApi::class.java)
    private val photosDao = mock(PhotosDao::class.java)
    private val photoRepository = PhotoRepository(flickrApi, photosDao, TestSchedulerProvider())

    @Test
    fun `Search photos using server`() {
        //arrange
        `when`(flickrApi.searchPhotos(latitude, longitude)).thenReturn(
            Observable.just(SearchPhotosResponse())
        )

        //act
        val result = photoRepository.searchPhotos(latitude, longitude)

        //assert
        result.test().assertValue(SearchPhotosResponse())
    }

    @Test
    fun `Get photos from database`() {
        //arrange
        val photos = listOf(Photo(url_c = "url"))
        `when`(photosDao.getAllPhotos()).thenReturn(
            Observable.just(photos)
        )

        //act
        val result = photoRepository.getPhotos()

        //assert
        result.test().assertValues(photos)
    }

    @Test
    fun `Insert photo into database`() {
        //arrange
        val photo = Photo()
        `when`(photosDao.insertAndGet(photo)).thenReturn(Single.just(photo))

        //act
        val result = photoRepository.insertPhoto(photo)

        //assert
        result.test().assertValue(photo)
        verify(photosDao).insertAndGet(photo)
    }
}