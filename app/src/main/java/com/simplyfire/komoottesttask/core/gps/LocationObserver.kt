package com.simplyfire.komoottesttask.core.gps

import android.location.Location

interface LocationObserver {
    fun onLocationUpdated(location: Location)
    fun onLocationAvailable(isAvailable: Boolean)
}

fun createLocationObserver(
    onLocationUpdated: (Location) -> Unit = {},
    onLocationAvailable: (Boolean) -> Unit = {}
): LocationObserver {

    return object : LocationObserver {
        override fun onLocationUpdated(location: Location) {
            onLocationUpdated(location)
        }

        override fun onLocationAvailable(isAvailable: Boolean) {
            onLocationAvailable(isAvailable)
        }
    }
}