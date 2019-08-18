package com.simplyfire.komoottesttask.core.di

import com.simplyfire.komoottesttask.core.di.module.AppModule
import com.simplyfire.komoottesttask.core.di.module.DbModule
import com.simplyfire.komoottesttask.core.di.module.NetworkModule
import com.simplyfire.komoottesttask.core.di.module.RxModule
import com.simplyfire.komoottesttask.feature.photolist.PhotoListActivity
import com.simplyfire.komoottesttask.feature.locationtracking.LocationTrackingService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RxModule::class, NetworkModule::class, DbModule::class])
interface AppComponent {
    fun inject(mainActivity: PhotoListActivity)
    fun inject(locationTrackingService: LocationTrackingService)
}