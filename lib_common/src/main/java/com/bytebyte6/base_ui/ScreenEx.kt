package com.bytebyte6.base_ui

import android.content.res.Resources

/**
 * px to dp
 */
val Int.px2dp: Int
    get() = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * dp to px
 */
val Int.dp2px: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val dp16=16.dp2px
val dp8=8.dp2px
val dp4=4.dp2px