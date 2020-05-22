package com.educatorapp.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    val calendar: Calendar = Calendar.getInstance()
    val TIMESTAMP_LONG_DATE_TIME_24_HOUR = "dd/MM/yyyy HH:mm:ss"
    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat(TIMESTAMP_LONG_DATE_TIME_24_HOUR)

    fun getCurrentTimestampUtcLong(): Long {
        return calendar.timeInMillis
    }

    fun getCurrentTimeString(): String {
        return simpleDateFormat.format(calendar.timeInMillis)
    }

    fun getCurrentTimeStamp(): Long {
        val stamp = Timestamp(System.currentTimeMillis())
        return stamp.time
    }
}