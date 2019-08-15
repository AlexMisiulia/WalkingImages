package com.simplyfire.komoottesttask.di

import com.simplyfire.komoottesttask.API_BASE_URL
import com.simplyfire.komoottesttask.FlickrApi
import com.simplyfire.komoottesttask.MainActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor



object Injector {
    fun inject(mainActivity: MainActivity) {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val flickrApi = Retrofit
            .Builder()
            .client(client)
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FlickrApi::class.java)
        mainActivity.flickrApi = flickrApi
    }
}