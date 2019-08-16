package com.simplyfire.komoottesttask.core.gps

import android.location.LocationListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


class LifecycleLocationListener(private val locationTracker: LocationTracker,
                                         private val lifecycle: Lifecycle,
                                         private val callback: LocationListener) :
    LifecycleObserver {
    private var enabled = false

    init {
        this.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun start() {
        if (enabled) {
            locationTracker.observeLocationChanges(callback)
        }
    }

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            locationTracker.observeLocationChanges(callback)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stop() {
        locationTracker.removeLocationListener(callback)
    }
}