package com.malikstudios.backtune.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {

    private const val PREF_NAME = "app_preferences"

    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // String
    var authToken: String?
        get() = preferences.getString("auth_token", null)
        set(value) = preferences.edit { putString("auth_token", value) }

    // Boolean
    var isFirstTimeLaunch: Boolean
        get() = preferences.getBoolean("first_time", true)
        set(value) = preferences.edit { putBoolean("first_time", value) }

    // Int
    var launchCount: Int
        get() = preferences.getInt("launch_count", 0)
        set(value) = preferences.edit { putInt("launch_count", value) }

    // Clear all
    fun clearAll() {
        preferences.edit { clear() }
    }

    var previousSavedYtUrl: String?
        get() = preferences.getString("previous_saved_yt_url", null)
        set(value) = preferences.edit {
            putString(
                "previous_saved_yt_url",
                if (value != null) "https://youtu.be/$value" else null
            )
        }
}
