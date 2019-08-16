package com.simplyfire.komoottesttask.core.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.simplyfire.komoottesttask.core.entity.Photo

@Dao
interface PhotosDao {
    @Query("SELECT * FROM Photo")
    fun getAllPhotos() : LiveData<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photo: Photo)
}