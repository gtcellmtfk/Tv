package com.bytebyte6.base

import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import kotlin.random.Random

fun randomColor(): Int {
    val dark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    val from = if (dark) 0 else 150
    val until = if (dark) 150 else 256
    val a = Random.Default.nextInt(0, 256)
    val r = Random.Default.nextInt(from, until)
    val g = Random.Default.nextInt(from, until)
    val b = Random.Default.nextInt(from, until)
    return Color.argb(a, r, g, b)
}

