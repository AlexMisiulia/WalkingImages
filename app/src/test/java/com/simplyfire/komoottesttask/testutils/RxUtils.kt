package com.simplyfire.komoottesttask.testutils

import com.simplyfire.komoottesttask.core.utils.SchedulerProvider
import io.reactivex.schedulers.Schedulers

class TestSchedulerProvider : SchedulerProvider {
    override fun computation() = Schedulers.trampoline()
    override fun main() = Schedulers.trampoline()
    override fun io() = Schedulers.trampoline()
}