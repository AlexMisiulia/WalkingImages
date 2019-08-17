package com.simplyfire.komoottesttask.core.gps

import android.annotation.SuppressLint
import android.app.Activity
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import javax.inject.Inject
import com.google.android.gms.location.LocationServices


private const val MIN_TIME = 0L
private const val MIN_DISTANCE = 100F

private const val TAG = "LocationTracker"

class LocationTracker @Inject constructor(private val fusedLocationProviderClient: FusedLocationProviderClient) {

    private val locationRequest = LocationRequest.create().apply {
        interval = MIN_TIME
        fastestInterval = MIN_TIME
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        smallestDisplacement = MIN_DISTANCE
    }

    private var locationCallback: LocationCallback? = null

    @SuppressLint("MissingPermission")
    fun observeLocationChanges(
        locationObserver: LocationObserver
    ) {
        // means that client code wants to subscribe once again
        if(locationCallback != null) return

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                Log.d(TAG, "onLocationResult, locationResult locations=${locationResult.lastLocation}}")
                locationObserver.onLocationUpdated(locationResult.lastLocation)
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                super.onLocationAvailability(locationAvailability)
                locationObserver.onLocationAvailable(locationAvailability.isLocationAvailable)
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, null
        )
    }

    fun removeLocationListener() {
        locationCallback?.let {
            fusedLocationProviderClient.removeLocationUpdates(it)
            locationCallback = null
        }
    }
}

fun checkLocationSettings(activity: Activity,
                          onSuccessResult: () -> Unit,
                          onFailureListener: (Throwable) -> Unit) {
    val locationRequest = LocationRequest.create().apply {
        interval = MIN_TIME
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val builder = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
    val client: SettingsClient = LocationServices.getSettingsClient(activity)
    val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

    task.addOnSuccessListener { locationSettingsResponse ->
        // All location settings are satisfied. The client can initialize
        // location requests here.
        onSuccessResult()
    }

    task.addOnFailureListener(onFailureListener)
}

fun requestLocationEnabling(exception: Throwable, activity: Activity, requestCode: Int) {
    if (exception is ResolvableApiException) {
        // Location settings are not satisfied, but this can be fixed
        // by showing the user a dialog.
        try {
            // Show the dialog by calling startResolutionForResult(),
            // and check the result in onActivityResult().
            exception.startResolutionForResult(activity, requestCode)
        } catch (sendEx: IntentSender.SendIntentException) {
            // Ignore the error.
        }
    }
}