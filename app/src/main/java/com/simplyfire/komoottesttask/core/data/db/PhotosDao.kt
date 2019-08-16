package com.simplyfire.komoottesttask.core.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simplyfire.komoottesttask.core.entity.Photo
import io.reactivex.Observable

@Dao
interface PhotosDao {
    @Query("SELECT * FROM Photo ORDER BY pkeyId DESC")
    fun getAllPhotos() : Observable<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)
}