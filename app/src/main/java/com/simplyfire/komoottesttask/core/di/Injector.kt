
package com.simplyfire.komoottesttask.core.di

import android.content.Context


@Suppress("ObjectPropertyName")
object Injector {

    val appComponent: AppComponent get() = _appComponent
    private lateinit var _appComponent: AppComponent

    fun init(context: Context) {
        _appComponent = DaggerAppComponent
            .builder()
            .appModule(AppModule(context))
            .build()
    }
}