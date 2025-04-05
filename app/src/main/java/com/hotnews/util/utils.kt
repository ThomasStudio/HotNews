package com.hotnews.util

import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by thomas on 3/4/2025.
 */

fun Long.toTimeStr(format: String = "yyyy-MM-dd HH:mm:ss"): String {
    return TimeToString(this, format)
}

fun TimeToString(timestamp: Long, format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    val date = Date(timestamp)
    return sdf.format(date)
}

fun String.urlEncode(): String = URLEncoder.encode(this, "UTF-8")