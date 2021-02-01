package com.bytebyte6.base

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun Int.dateString(): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(this*1000L))
}

@SuppressLint("SimpleDateFormat")
fun Int.dateStringhhmm(): String {
    return SimpleDateFormat("hh:mm").format(Date(this*1000L))
}