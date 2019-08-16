package com.simplyfire.komoottesttask.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.simplyfire.komoottesttask.BuildConfig
import com.simplyfire.komoottesttask.core.entity.Photo

const val DATABASE_NAME = BuildConfig.APPLICATION_ID

@Database(entities = [Photo::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun photosDao(): PhotosDao
}