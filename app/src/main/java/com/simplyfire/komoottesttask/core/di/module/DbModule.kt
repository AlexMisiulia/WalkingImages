package com.simplyfire.komoottesttask.core.di.module

import android.content.Context
import androidx.room.Room
import com.simplyfire.komoottesttask.core.data.db.AppDatabase
import com.simplyfire.komoottesttask.core.data.db.DATABASE_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun providesPhotosDao(appDatabase: AppDatabase) = appDatabase.photosDao()
}