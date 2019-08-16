package com.simplyfire.komoottesttask.feature.photolist

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
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
import android.app.PendingIntent

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID
private const val NOTIFICATION_ID = 888
private const val NOTIFICATIONS_CHANNEL_NAME = "Waling images service"

private const val TAG = "LocationTrackingService"

class LocationTrackingService : Service() {
    @Inject lateinit var photoRepository: PhotoRepository
    @Inject lateinit var locationTracker: LocationTracker

    private val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val locationListener = createLocationListener(onLocationChangedListener = {
        photoRepository.searchPhoto(it.latitude.toString(), it.longitude.toString())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = {
                updateNotification("Получено новое фото")
            }, onError = { error ->
                Log.e(TAG, "error during searching photos", error)
            })
    })

    private fun updateNotification(text: String) {
        notificationManager.notify(NOTIFICATION_ID, buildNotification(text = text))
    }

    private fun buildNotification(
        title: String = getString(com.simplyfire.komoottesttask.R.string.notification_title),
        text: String = "",
        initChannel : Boolean = false
    ): Notification {
        if (initChannel && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        val contentIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, PhotoListActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT
        )
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(contentIntent)
            .setSmallIcon(com.simplyfire.komoottesttask.R.drawable.ic_launcher_foreground)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val chan = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATIONS_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_NONE
        )
        notificationManager.createNotificationChannel(chan)
    }

    override fun onCreate() {
        super.onCreate()
        Injector.appComponent.inject(this)
        startForeground(NOTIFICATION_ID, buildNotification(initChannel = true))
        locationTracker.observeLocationChanges(locationListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        locationTracker.removeLocationListener(locationListener)
    }
}