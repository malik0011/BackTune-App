package com.malikstudios.backtune.utils

import com.malikstudios.backtune.BuildConfig

/**
 * Utility object for centralized and controlled logging.
 * Automatically disables logs in Release builds using BuildConfig.DEBUG flag.
 */
object LogUtils {

    // Flag to control logging based on build type.
    // Logs will only be printed in Debug builds.
    private val isDebug = BuildConfig.DEBUG

    /**
     * Log a debug message.
     * Used for general debug information during development.
     *
     * @param tag Tag identifying the source of a log message.
     * @param message The message to log.
     */
    fun d(tag: String, message: String) {
        if (isDebug) {
            android.util.Log.d(tag, message)
        }
    }

    /**
     * Log an info message.
     * Used for highlighting routine information during app execution.
     *
     * @param tag Tag identifying the source of a log message.
     * @param message The message to log.
     */
    fun i(tag: String, message: String) {
        if (isDebug) {
            android.util.Log.i(tag, message)
        }
    }

    /**
     * Log an error message.
     * Used when errors occur that should be visible during development.
     *
     * @param tag Tag identifying the source of a log message.
     * @param message The message to log.
     * @param throwable Optional throwable to log stack trace.
     */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug) {
            if (throwable != null) {
                android.util.Log.e(tag, message, throwable)
            } else {
                android.util.Log.e(tag, message)
            }
        }
    }

    /**
     * Log a warning message.
     * Used to highlight potential issues in the app flow.
     *
     * @param tag Tag identifying the source of a log message.
     * @param message The message to log.
     */
    fun w(tag: String, message: String) {
        if (isDebug) {
            android.util.Log.w(tag, message)
        }
    }

    /**
     * Log a verbose message.
     * Used for highly detailed logs, typically too verbose for regular use.
     *
     * @param tag Tag identifying the source of a log message.
     * @param message The message to log.
     */
    fun v(tag: String, message: String) {
        if (isDebug) {
            android.util.Log.v(tag, message)
        }
    }
}
