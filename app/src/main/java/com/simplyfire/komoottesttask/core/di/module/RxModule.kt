package com.simplyfire.komoottesttask.core.di.module

import com.simplyfire.komoottesttask.core.utils.AndroidSchedulerProvider
import com.simplyfire.komoottesttask.core.utils.SchedulerProvider
import dagger.Binds
import dagger.Module

@Module
abstract class RxModule {
    @Binds abstract fun providesScheduler(scheduler: AndroidSchedulerProvider) : SchedulerProvider
}