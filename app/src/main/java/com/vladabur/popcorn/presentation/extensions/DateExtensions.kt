package com.vladabur.popcorn.presentation.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Date.toYearAndMonth(): String {
    val dateFormat = SimpleDateFormat("MMM yyyy", Locale.getDefault())
    return dateFormat.format(this)
}