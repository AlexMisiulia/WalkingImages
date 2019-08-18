package com.simplyfire.komoottesttask.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simplyfire.komoottesttask.core.entity.Photo
import io.reactivex.Observable
import io.reactivex.Single

@Dao
abstract class PhotosDao {
    @Query("SELECT * FROM Photo ORDER BY pkeyId DESC")
    abstract fun getAllPhotos() : Observable<List<Photo>>

    @Query("SELECT * FROM Photo WHERE pkeyId == :id")
    abstract fun getPhoto(id: Int) : Single<Photo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(photo: Photo) : Long

    fun insertAndGet(photo: Photo) : Single<Photo> {
        val id = insert(photo)
        return getPhoto(id.toInt())
    }
}