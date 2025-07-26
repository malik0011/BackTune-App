package com.malikstudios.backtune

import android.app.Application
import android.content.Intent
import android.util.Log
import com.google.firebase.FirebaseApp
import com.malikstudios.backtune.utils.AppPreferences
import com.malikstudios.backtune.utils.LogUtils
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import com.onesignal.debug.OneSignalLogEvent
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BackTuneApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize SharedPreferences
        AppPreferences.init(this)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        val apiKey = BuildConfig.MY_SECRET_API_KEY
        // Initialize OneSignal
        OneSignal.initWithContext(this, apiKey)

        // Set logging level (Optional)
        OneSignal.Debug.addLogListener {
            event: OneSignalLogEvent ->
            //log them dirrently with same tag name but different log level
            when (event.level) {
                LogLevel.NONE -> {}
                LogLevel.FATAL -> {
                    //log fatal errors
                    LogUtils.e("OneSignal", "Fatal error: ${event.entry}")
                }
                LogLevel.ERROR -> {
                    //log errors
                    LogUtils.e("OneSignal", "Error: ${event.entry}")
                }
                LogLevel.WARN ->  {
                    //log warnings
                    LogUtils.w("OneSignal", "Warning: ${event.entry}")
                }
                LogLevel.INFO -> {
                    //log info messages
                    LogUtils.i("OneSignal", "Info: ${event.entry}")
                }
                LogLevel.DEBUG -> {
                    //log debug messages
                    LogUtils.d("OneSignal", "Debug: ${event.entry}")
                }
                LogLevel.VERBOSE -> {
                    //log verbose messages
                    LogUtils.v("OneSignal", "Verbose: ${event.entry}")
                }
            }
        }

        // Notification Opened Handler
        OneSignal.Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val data = event.notification.additionalData
                val customValue = data?.optString("yt_link", null) ?: "No custom value"
                LogUtils.d("OneSignal", "onClick: $customValue")


                val intent = Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("yt_link", customValue)
                }

                startActivity(intent)  // applicationContext required since you're in Application class
            }
        })
    }
} 