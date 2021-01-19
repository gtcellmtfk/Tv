package com.bytebyte6.base

import android.graphics.Color
import androidx.appcompat.app.AppCompatDelegate
import kotlin.random.Random

fun randomColorByNightMode(): Int {
    val dark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    val from = if (dark) 0 else 200
    val until = if (dark) 100 else 256
    val a = 255/* Random.Default.nextInt(from, until)*/
    val r = Random.Default.nextInt(from, until)
    val g = Random.Default.nextInt(from, until)
    val b = Random.Default.nextInt(from, until)
    return Color.argb(a, r, g, b)
}

fun randomColor(): Int {
    val from = 0
    val until = 256
    val a = Random.Default.nextInt(from, until)
    val r = Random.Default.nextInt(from, until)
    val g = Random.Default.nextInt(from, until)
    val b = Random.Default.nextInt(from, until)
    return Color.argb(a, r, g, b)
}