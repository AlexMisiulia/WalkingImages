package com.simplyfire.komoottesttask.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle

@SuppressLint("MissingPermission")
fun Context.observeLocationChanges(locationListener: LocationListener) {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    locationManager.requestLocationUpdates(
        LocationManager.GPS_PROVIDER, 5000, 100F, locationListener
    )
}

fun Context.removeLocationListener(locationListener: LocationListener) {
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    locationManager.removeUpdates(locationListener)
}

fun createLocationListener(onLocationChangedListener: (Location) -> Unit): LocationListener {
    return object: LocationListener {

        override fun onLocationChanged(location: Location) {
            onLocationChangedListener(location)
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
}