package com.simplyfire.komoottesttask.core.gps

import android.annotation.SuppressLint
import android.location.LocationListener
import android.location.LocationManager
import javax.inject.Inject
import javax.inject.Singleton

private const val MIN_TIME = 0L
private const val MIN_DISTANCE = 100F

@Singleton
class LocationTracker @Inject constructor(private val locationManager: LocationManager) {

    @SuppressLint("MissingPermission")
    fun observeLocationChanges(
        locationListener: LocationListener,
        minTime: Long = MIN_TIME,
        minDistance: Float = MIN_DISTANCE
    ) {
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener
        )
    }

    fun removeLocationListener(locationListener: LocationListener) {
        locationManager.removeUpdates(locationListener)
    }
}