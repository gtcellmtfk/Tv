package com.bytebyte6.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Toast
 * */
fun Context.showToast(it: Message) {
    Toast.makeText(
        this,
        it.get(this),
        if (it.longDuration) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    ).show()
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