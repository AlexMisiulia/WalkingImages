package com.simplyfire.komoottesttask.core.utils

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

fun createLocationListener(
    onLocationChangedListener: (Location) -> Unit = {},
    onProviderDisabledListener: (String) -> Unit = {}
): LocationListener {
    return object : LocationListener {

        override fun onLocationChanged(location: Location) {
            onLocationChangedListener(location)
        }

        override fun onProviderDisabled(provider: String) {
            onProviderDisabledListener(provider)
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
}