package com.simplyfire.komoottesttask

import android.app.Application
import com.simplyfire.komoottesttask.core.di.Injector

class FlickrApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.init(this)
    }
}