package com.simplyfire.komoottesttask.core.gps

import android.content.Context
import android.location.LocationListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.LifecycleObserver
import com.simplyfire.komoottesttask.core.utils.observeLocationChanges
import com.simplyfire.komoottesttask.core.utils.removeLocationListener


internal class LifecycleLocationListener(private val context: Context,
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
            context.observeLocationChanges(callback)
        }
    }

    fun enable() {
        enabled = true
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            context.observeLocationChanges(callback)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stop() {
        context.removeLocationListener(callback)
    }
}