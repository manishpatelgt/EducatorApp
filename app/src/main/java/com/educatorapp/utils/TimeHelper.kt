package com.educatorapp.utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    val calendar: Calendar = Calendar.getInstance()
    val TIMESTAMP_LONG_DATE_TIME_24_HOUR = "dd/MM/yyyy HH:mm:ss"
    val TIMESTAMP_LONG_DATE_TIME_24_HOUR_2 = "dd MMM hh:mm aa"
    var simpleDateFormat: SimpleDateFormat = SimpleDateFormat(TIMESTAMP_LONG_DATE_TIME_24_HOUR)
    var simpleDateFormat2: SimpleDateFormat = SimpleDateFormat(TIMESTAMP_LONG_DATE_TIME_24_HOUR_2)

    fun getStringToDate(date: String): String {
        val dateTime = simpleDateFormat.parse(date)
        return simpleDateFormat2.format(dateTime)
    }

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