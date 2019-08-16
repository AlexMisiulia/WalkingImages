package com.simplyfire.komoottesttask.core.di

import android.content.Context
import androidx.room.Room
import com.simplyfire.komoottesttask.core.data.db.AppDatabase
import com.simplyfire.komoottesttask.core.data.db.DATABASE_NAME
import com.simplyfire.komoottesttask.core.network.API_BASE_URL
import com.simplyfire.komoottesttask.core.network.FlickrApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    fun providesContext() = context

    @Singleton
    @Provides
    fun providesRetrofit() : Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesFlickrApi(retrofit: Retrofit) = retrofit.create(FlickrApi::class.java)

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