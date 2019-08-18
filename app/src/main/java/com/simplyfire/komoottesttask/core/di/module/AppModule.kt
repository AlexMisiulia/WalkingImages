package com.simplyfire.komoottesttask.core.di.module

import android.content.Context
import android.location.LocationManager
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.simplyfire.komoottesttask.core.data.db.AppDatabase
import com.simplyfire.komoottesttask.core.data.db.DATABASE_NAME
import com.simplyfire.komoottesttask.core.data.network.API_BASE_URL
import com.simplyfire.komoottesttask.core.data.network.FlickrApi
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
    fun providesLocationManager(context: Context): LocationManager {
        return context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Singleton
    @Provides
    fun providesFusedLocationManager(context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
}