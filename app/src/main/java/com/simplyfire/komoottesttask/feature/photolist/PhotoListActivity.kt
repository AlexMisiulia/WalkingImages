package com.simplyfire.komoottesttask.feature.photolist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simplyfire.komoottesttask.R
import com.simplyfire.komoottesttask.core.di.Injector
import com.simplyfire.komoottesttask.core.di.ViewModelFactory
import com.simplyfire.komoottesttask.core.utils.observeLocationChanges
import com.simplyfire.komoottesttask.core.utils.removeLocationListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

private const val LOCATION_PERMISSION_REQUEST_CODE = 777
private const val TAG = "PhotoListActivity"

class PhotoListActivity : AppCompatActivity() {
    private val adapter = PhotoAdapter()

    @Inject lateinit var viewModelFactory: ViewModelFactory<PhotoListViewModel>
    private lateinit var viewModel: PhotoListViewModel

    private lateinit var startStopMenuItem: MenuItem

    private val locationListener = object: LocationListener {

        override fun onLocationChanged(location: Location) {

        }

        override fun onProviderDisabled(provider: String) {
            onGpsDisabled()
        }

        override fun onProviderEnabled(provider: String) {

        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.appComponent.inject(this)

        initView()
        initViewModel()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotoListViewModel::class.java)
        viewModel.viewState.observe(this, Observer {
            adapter.submitList(it.photos)
        })
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun updateStartStopButtonState(isLocationTrackingActive: Boolean) {
        if(isLocationTrackingActive) {
            startStopMenuItem.title = getText(R.string.stop)
        } else {
            startStopMenuItem.title = getText(R.string.start)
        }
    }

    private fun onDisabledLocationPermission() {
        AlertDialog.Builder(this)
            .setMessage(R.string.location_permission_error_msg)
            .setNeutralButton(android.R.string.ok) { _, _ ->
                startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }.show()
    }

    private fun onGpsDisabled() {
        AlertDialog.Builder(this)
            .setMessage(R.string.location_disabled_error_msg)
            .setNeutralButton(android.R.string.ok) { _, _ ->
                startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }.show()
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    startLocationTracking()
                } else {
                    onDisabledLocationPermission()
                }
                return
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.photo_list_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        startStopMenuItem = menu.findItem(R.id.start_stop_button)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.start_stop_button -> {
                onStartStopButtonClicked()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun onStartStopButtonClicked() {
        when(startStopMenuItem.title) {
            getString(R.string.start) -> startLocationTracking()
            getString(R.string.stop) -> stopLocationTracking()
            else -> throw IllegalArgumentException("Unknown button text=${startStopMenuItem.title}")
        }
    }

    private fun stopLocationTracking() {
        stopService(Intent(this, SynchronizationService::class.java))
        updateStartStopButtonState(false)
    }

    private fun startLocationTracking() {
        if(hasLocationPermission()) {
            startService(Intent(this, SynchronizationService::class.java))
            updateStartStopButtonState(true)
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        observeLocationChanges(locationListener)
    }

    override fun onPause() {
        super.onPause()
        removeLocationListener(locationListener)
    }
}
