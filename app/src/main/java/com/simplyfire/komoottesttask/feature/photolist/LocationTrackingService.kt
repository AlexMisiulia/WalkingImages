package com.simplyfire.komoottesttask.feature.photolist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.simplyfire.komoottesttask.BuildConfig
import com.simplyfire.komoottesttask.core.data.PhotoRepository
import com.simplyfire.komoottesttask.core.di.Injector
import com.simplyfire.komoottesttask.core.gps.LocationTracker
import com.simplyfire.komoottesttask.core.utils.createLocationListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID
private const val NOTIFICATIONS_CHANNEL_NAME = "Waling images service"

private const val TAG = "LocationTrackingService"
class LocationTrackingService : Service() {
    @Inject lateinit var photoRepository: PhotoRepository
    @Inject lateinit var locationTracker: LocationTracker

    private val locationListener = createLocationListener(onLocationChangedListener = {
        photoRepository.searchPhoto(it.latitude.toString(), it.longitude.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onError = { error ->
                Log.e(TAG, "error during searching photos", error)
            })
    })

    private fun runAsForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startMyOwnForeground()
        else
            startForeground(1, Notification())
    }

    override fun onCreate() {
        super.onCreate()
        Injector.appComponent.inject(this)
        runAsForeground()

        locationTracker.observeLocationChanges(locationListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun startMyOwnForeground() {
        val chan = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATIONS_CHANNEL_NAME, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(chan)

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle("App is running in background")
            .setPriority(NotificationManager.IMPORTANCE_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(2, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationTracker.removeLocationListener(locationListener)
    }
}