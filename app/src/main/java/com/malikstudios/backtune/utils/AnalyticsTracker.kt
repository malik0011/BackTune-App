package com.malikstudios.backtune.utils

import android.content.Context
import android.os.Bundle
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.malikstudios.backtune.BuildConfig

object AnalyticsTracker {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    fun init(context: Context) {
        firebaseAnalytics = Firebase.analytics
    }

    fun logAppOpen() {
        logEvent(FirebaseAnalytics.Event.APP_OPEN)
    }

    fun appOpen() {
        logEvent(FirebaseAnalytics.Event.APP_OPEN,
            "app_version" to BuildConfig.VERSION_NAME,
            "build_number" to BuildConfig.VERSION_CODE,
            "device_model" to android.os.Build.MODEL,
            "app_opened_at" to System.currentTimeMillis()
        )
    }

    fun logLoginSuccess(userId: String) {
        logEvent(
            Event.LOGIN_SUCCESS,
            "user_id" to userId
        )
    }

    fun logScreenView(screenName: String, screenClass: String) {
        logEvent(
            FirebaseAnalytics.Event.SCREEN_VIEW,
            FirebaseAnalytics.Param.SCREEN_NAME to screenName,
            FirebaseAnalytics.Param.SCREEN_CLASS to screenClass
        )
    }

    fun logCustomEvent(eventName: String, vararg params: Pair<String, String>) {
        logEvent(eventName, *params)
    }

    private fun logEvent(eventName: String, vararg params: Pair<String, Any?>) {
        val bundle = Bundle()
        for ((key, value) in params) {
            when (value) {
                is String -> bundle.putString(key, value)
                is Int -> bundle.putInt(key, value)
                is Long -> bundle.putLong(key, value)
                is Double -> bundle.putDouble(key, value)
                is Boolean -> bundle.putBoolean(key, value)
                else -> continue
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    object Event {
        const val LOGIN_SUCCESS = "login_success"
        const val LOGOUT = "logout"
        const val USER_PROFILE_UPDATED = "user_profile_updated"
        // Add more custom events here
    }
}