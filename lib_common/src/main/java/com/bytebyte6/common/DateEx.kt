package com.bytebyte6.common

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Int.yyyyMMdd(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(this * 1000L))
}

@SuppressLint("SimpleDateFormat")
fun Int.yyyyMMddhhmm(): String {
    return SimpleDateFormat("yyyy-MM-dd hh:mm").format(Date(this * 1000L))
}

@SuppressLint("SimpleDateFormat")
fun Int.yyyyMMddhhmmss(): String {
    return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(this * 1000L))
}

@SuppressLint("SimpleDateFormat")
fun Int.hhmm(): String {
    return SimpleDateFormat("hh:mm").format(Date(this * 1000L))
}

@SuppressLint("SimpleDateFormat")
fun Int.hhmmss(): String {
    return SimpleDateFormat("hh:mm:ss").format(Date(this * 1000L))
}
