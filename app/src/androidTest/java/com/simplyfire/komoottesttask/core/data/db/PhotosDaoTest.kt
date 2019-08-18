package com.simplyfire.komoottesttask.core.data.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.simplyfire.komoottesttask.core.entity.Photo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PhotosDaoTest {
    @JvmField @Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var photosDao: PhotosDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java).build()
        photosDao = db.photosDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllPhotos_returnDescSortedPhotos() {
        //arrange
        val firstPhoto = Photo(1, url_c = "firstPhoto")
        val secondPhoto = Photo(2, url_c = "secondPhoto")
        photosDao.insert(firstPhoto)
        photosDao.insert(secondPhoto)

        //act
        val result = photosDao.getAllPhotos()

        //assert
        result.test().assertValues(listOf(secondPhoto, firstPhoto))
    }

    @Test
    fun getPhoto_successfullyGetInsertedPhotoById() {
        //arrange
        val photo = Photo(1, url_c = "firstPhoto")
        photosDao.insert(photo)

        //act
        val result = photosDao.getPhoto(1)

        //assert
        result.test().assertValue(photo)
    }

    @Test
    fun insertAndGet_insertedValueIsStoredInDb() {
        //arrange
        val photo = Photo(1, url_c = "firstPhoto")
        photosDao.insertAndGet(photo)

        //act
        val result = photosDao.getAllPhotos()

        //assert
        result.test().assertValues(listOf(photo))
    }

}