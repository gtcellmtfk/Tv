package com.bytebyte6.common

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Toast
 * */
fun Fragment.showToast(it: Message) {
    requireContext().showToast(it)
}

fun Activity.showToast(it: Message) {
    baseContext.showToast(it)
}

fun Context.showToast(it: Message) {
    Toast.makeText(
        this,
        it.get(this),
        if (it.longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
}

/**
 * SnackBar
 * */
fun Fragment.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener? = null
) {
    requireContext().showSnack(view, it, listener)
}

fun Activity.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener?
) {
    baseContext.showSnack(view, it, listener)
}

fun Context.showSnack(
    view: View,
    it: Message,
    listener: View.OnClickListener? = null
) {
    val bar = Snackbar.make(
        view,
        it.get(this),
        if (it.longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    )
    if (it.actionStringId != 0) {
        bar.setAction(it.actionStringId, listener)
    }
    bar.show()
}

fun Context.showSnack(
    view: View,
    messageId: Int,
    listener: View.OnClickListener? = null,
    longDuration: Boolean = true,
    actionStringId: Int = 0
) {
    showSnack(view, getString(messageId), listener, longDuration, actionStringId)
}

fun Fragment.showSnack(
    view: View,
    messageId: Int,
    listener: View.OnClickListener? = null
) {
    requireContext().showSnack(view, getString(messageId), listener)
}

fun Fragment.showSnack(
    view: View,
    message: String,
    listener: View.OnClickListener? = null
) {
    requireContext().showSnack(view, message, listener)
}

fun Activity.showSnack(
    view: View,
    message: String,
    listener: View.OnClickListener?
) {
    view.context.showSnack(view, message, listener)
}

fun Context.showSnack(
    view: View,
    message: String,
    listener: View.OnClickListener? = null,
    longDuration: Boolean = true,
    actionStringId: Int = 0
) {
    val bar = Snackbar.make(
        view,
        message,
        if (longDuration) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
    )
    if (actionStringId != 0) {
        bar.setAction(actionStringId, listener)
    }
    bar.show()
}

/**
 * NavigationView Item Background
 */
fun navigationItemBackground(context: Context): Drawable? {
    var background =
        AppCompatResources.getDrawable(context, R.drawable.navigation_item_background)
    if (background != null) {
        val tint = AppCompatResources.getColorStateList(
            context, R.color.navigation_item_background_tint
        )
        background = DrawableCompat.wrap(background.mutate())
        background.setTintList(tint)
    }
    return background
}