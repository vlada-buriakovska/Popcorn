package com.vladabur.popcorn.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun Date.toYearAndMonth(): String {
    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date?.isSameMonthAndYear(anotherDate: Date?): Boolean {
    if (this == null || anotherDate == null) return false
    val firstDateCalendar = Calendar.getInstance()
    val secondDateCalendar = Calendar.getInstance()
    firstDateCalendar.time = this
    secondDateCalendar.time = anotherDate
    return firstDateCalendar[Calendar.YEAR] == secondDateCalendar[Calendar.YEAR]
            && firstDateCalendar[Calendar.MONTH] == secondDateCalendar[Calendar.MONTH]
}