package com.bytebyte6.common

import android.view.View
import com.google.android.material.snackbar.Snackbar

inline fun View.longSnack(
    msg: Int,
    actionSetup: Snackbar.() -> Unit = {}
) = snack(context.getString(msg), Snackbar.LENGTH_LONG, actionSetup)

inline fun View.snack(
    msg: CharSequence,
    duration: Int = Snackbar.LENGTH_SHORT,
    actionSetup: Snackbar.() -> Unit = {}
) = Snackbar.make(this, msg, duration).apply(actionSetup).also { it.show() }

inline fun View.longSnack(
    msg: CharSequence,
    actionSetup: Snackbar.() -> Unit = {}
) = snack(msg, Snackbar.LENGTH_LONG, actionSetup)