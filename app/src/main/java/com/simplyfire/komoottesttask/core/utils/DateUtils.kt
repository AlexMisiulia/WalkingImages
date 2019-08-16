package com.simplyfire.komoottesttask.core.utils

import java.text.SimpleDateFormat
import java.util.*

val notificationFormat = SimpleDateFormat("HH:mm:ss", Locale.ROOT)

fun formatDate(date: Date, format: SimpleDateFormat): String {
    return format.format(date)
}

