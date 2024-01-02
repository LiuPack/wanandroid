package org.liupack.wanandroid.common

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatGMT8(date: Long): String = Instant.fromEpochMilliseconds(date).toLocalDateTime(
    TimeZone.of("GMT+8")
).let {
    val year = it.year
    val month = it.monthNumber.toString().padStart(2, '0')
    val day = it.dayOfMonth.toString().padStart(2, '0')
    val hour = it.hour.toString().padStart(2, '0')
    val minute = it.minute.toString().padStart(2, '0')
    val second = it.second.toString().padStart(2, '0')
    buildString {
        append(year).append("-").append(month).append("-").append(day).append(" ")
            .append(hour).append(":").append(minute).append(":").append(second)
    }
}