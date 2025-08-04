package com.malikstudios.backtune.utils

/*
    TimeUtils.kt
    A utility object for handling time-related operations, such as converting timestamps to human-readable formats.
    This object provides a method to get a relative time string for a given past time in milliseconds.
 */
object TimeUtils {

    /**
    * Returns a human-readable relative time string for a given past time in milliseconds.
    * For example, if the input is 5 minutes ago, it returns "5 minutes ago".
    * If the input is in the future, it returns "in the future".
    * If the input is less than 10 seconds ago, it returns "just now".
    * @param pastTimeMillis The time in milliseconds from the past.
    */
    fun getRelativeTime(pastTimeMillis: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - pastTimeMillis

        if (diff < 0) return "in the future"

        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30
        val years = days / 365

        return when {
            seconds < 10 -> "just now"
            seconds < 60 -> pluralize(seconds, "second")
            minutes < 60 -> pluralize(minutes, "minute")
            hours < 24 -> pluralize(hours, "hour")
            days < 30 -> pluralize(days, "day")
            months < 12 -> pluralize(months, "month")
            years == 1L -> "1 year ago"
            else -> "$years years ago"
        }
    }

    /**
     * Returns a pluralized string for a given value and unit.
     * For example, if the value is 5 and the unit is "minute", it returns "5 minutes ago".
     * If the value is 1, it returns "1 minute ago".
     * @param value The numeric value to pluralize.
     * @param unit The unit of time (e.g., "second", "minute", "hour").
     */
    private fun pluralize(value: Long, unit: String): String {
        return if (value == 1L) {
            "1 $unit ago"
        } else {
            "$value ${unit}s ago"
        }
    }
}
