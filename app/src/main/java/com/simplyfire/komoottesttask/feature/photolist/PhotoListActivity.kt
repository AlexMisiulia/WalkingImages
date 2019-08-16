package com.simplyfire.komoottesttask.feature.photolist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simplyfire.komoottesttask.R
import com.simplyfire.komoottesttask.core.di.Injector
import kotlinx.android.synthetic.main.activity_main.*

private const val LOCATION_PERMISSION_REQUEST_CODE = 777
private const val TAG = "PhotoListActivity"

class PhotoListActivity : AppCompatActivity() {
    private val adapter = PhotoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Injector.appComponent.inject(this)

        initView()

        checkLocationPermission()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recyclerView.adapter = adapter
    }

    private fun onEnabledLocationPermission() {
        startService(Intent(this, SynchronizationService::class.java))
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
        } else {
            onEnabledLocationPermission()
        }
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
}
