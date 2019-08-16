package com.simplyfire.komoottesttask.core.di

import android.content.Context
import com.simplyfire.komoottesttask.core.network.API_BASE_URL
import com.simplyfire.komoottesttask.core.network.FlickrApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule(private val context: Context) {

    @Provides
    fun providesContext() = context

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

    @Provides
    fun providesFlickrApi(retrofit: Retrofit) = retrofit.create(FlickrApi::class.java)
}