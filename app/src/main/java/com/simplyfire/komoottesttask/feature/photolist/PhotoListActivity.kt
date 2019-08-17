package com.simplyfire.komoottesttask.feature.photolist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.simplyfire.komoottesttask.core.gps.LifecycleLocationListener
import com.simplyfire.komoottesttask.core.gps.LocationTracker
import com.simplyfire.komoottesttask.core.utils.createLocationListener
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.IllegalArgumentException
import javax.inject.Inject

private const val LOCATION_PERMISSION_REQUEST_CODE = 777
private const val TAG = "PhotoListActivity"

class PhotoListActivity : AppCompatActivity() {
    private val adapter = PhotoAdapter()

    @Inject lateinit var viewModelFactory: ViewModelFactory<PhotoListViewModel>
    @Inject lateinit var locationTracker: LocationTracker
    private lateinit var viewModel: PhotoListViewModel

    private var startStopMenuItem: MenuItem? = null

    private lateinit var lifecycleLocationListener: LifecycleLocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.appComponent.inject(this)

        initView()
        initViewModel()
        initLocationListener()
    }

    private fun initLocationListener() {
        lifecycleLocationListener = LifecycleLocationListener(locationTracker, lifecycle,
            createLocationListener(onProviderDisabledListener = {
                viewModel.onLocationDisabled()
            })
        )
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(PhotoListViewModel::class.java)
        viewModel.viewState.observe(this, Observer {
            render(it)
        })
    }

    private fun render(it: PhotoListViewModel.ViewState) {
        adapter.submitList(it.photos)

        it.startLocationTracker.getContentIfNotHandled()?.let {
            if(it) startLocationTracking()
        }

        it.stopLocationTracker.getContentIfNotHandled()?.let {
            if(it) stopLocationTracking()
        }

        it.requestLocationPermission.getContentIfNotHandled()?.let {
            if(it) requestLocationPermission()
        }

        updateStartStopButtonState(it.isLocationTrackingActive)

        it.error.getContentIfNotHandled()?.let {
            when (it) {
                PhotoListViewModel.Error.LOCATION_PERMISSION_DENIED -> showLocationPermissionDenied()
                PhotoListViewModel.Error.LOCATION_DISABLED -> showLocationDisabledError()
            }
        }
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun updateStartStopButtonState(isLocationTrackingActive: Boolean) {
        if(isLocationTrackingActive) {
            startStopMenuItem?.title = getText(R.string.stop)
        } else {
            startStopMenuItem?.title = getText(R.string.start)
        }
    }

    private fun showLocationPermissionDenied() {
        AlertDialog.Builder(this)
            .setMessage(R.string.location_permission_error_msg)
            .setNeutralButton(android.R.string.ok) { _, _ ->
                startActivity(Intent(ACTION_LOCATION_SOURCE_SETTINGS))
            }.show()
    }

    private fun showLocationDisabledError() {
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
                    viewModel.onLocationPermissionGranted()
                } else {
                    viewModel.onLocationPermissionDenied()
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
        when(startStopMenuItem?.title) {
            getString(R.string.start) -> viewModel.onStartClicked(hasLocationPermission())
            getString(R.string.stop) -> viewModel.onStopClicked()
            else -> throw IllegalArgumentException("Unknown button text=${startStopMenuItem?.title}")
        }
    }

    private fun stopLocationTracking() {
        stopService(Intent(this, LocationTrackingService::class.java))
    }

    private fun startLocationTracking() {
        lifecycleLocationListener.enable()
        startService(Intent(this, LocationTrackingService::class.java))
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
}
