package com.simplyfire.komoottesttask.core.utils

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface SchedulerProvider {
    fun io(): Scheduler
    fun computation(): Scheduler
    fun main(): Scheduler
}

@Singleton
class AndroidSchedulerProvider @Inject constructor() : SchedulerProvider {
    override fun computation() = Schedulers.computation()
    override fun main() = AndroidSchedulers.mainThread()
    override fun io() = Schedulers.io()
}

