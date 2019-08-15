package com.simplyfire.komoottesttask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simplyfire.komoottesttask.di.Injector
import com.simplyfire.komoottesttask.entity.SearchPhotosResponse
import com.simplyfire.komoottesttask.feature.photolist.DisplayablePhoto
import com.simplyfire.komoottesttask.feature.photolist.PhotoAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val LOCATION_PERMISSION_REQUEST_CODE = 777
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    lateinit var flickrApi: FlickrApi
    private val adapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.inject(this)

        initView()

        checkLocationPermission()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun onEnabledLocationPermission() {
        observeLocationChanges {
            flickrApi.getPhotosForLocation(it.latitude.toString(), it.longitude.toString()).enqueue(
                object : Callback<SearchPhotosResponse> {
                    override fun onFailure(call: Call<SearchPhotosResponse>, t: Throwable) {
                        showErrorDialog(t)
                    }

                    override fun onResponse(
                        call: Call<SearchPhotosResponse>,
                        response: Response<SearchPhotosResponse>
                    ) {
                        val result = response.body()!!.photos.photo.map {photo -> DisplayablePhoto(photo.url_c) }.filter { it.url.isNotBlank() }
                        Log.d(TAG, "response=$result")

                        adapter.submitList(result)
                    }

                })
        }
    }

    private fun showErrorDialog(t: Throwable) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(R.string.general_error)
            .setNeutralButton(android.R.string.ok) { _, _ ->
                checkLocationPermission()
            }.show()
        Log.e(TAG, "error during request", t)
    }

    private fun onDisabledLocationPermission() {
        AlertDialog.Builder(this)
            .setMessage(R.string.location_permission_error_msg)
            .setNeutralButton(android.R.string.ok) { _, _ ->
                checkLocationPermission()
            }.show()
    }

    private fun checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }

        onEnabledLocationPermission()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    onEnabledLocationPermission()
                } else {
                    onDisabledLocationPermission()
                }
                return
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun observeLocationChanges(onLocationChanged: (Location) -> Unit) {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = SimpleLocationListener(onLocationChanged)
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 5000, 100F, locationListener
        )
    }

    /*---------- Listener class to get coordinates ------------- */
    private inner class SimpleLocationListener(private val onLocationChangedListener: (Location) -> Unit) : LocationListener {

        override fun onLocationChanged(location: Location) {
            onLocationChangedListener(location)
        }

        override fun onProviderDisabled(provider: String) {}

        override fun onProviderEnabled(provider: String) {}

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
}
