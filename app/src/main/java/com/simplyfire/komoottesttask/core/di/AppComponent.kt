package com.simplyfire.komoottesttask.core.di

import com.simplyfire.komoottesttask.feature.photolist.PhotoListActivity
import com.simplyfire.komoottesttask.feature.photolist.SynchronizationService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: PhotoListActivity)
    fun inject(synchronizationService: SynchronizationService)
}