package com.bytebyte6.base_ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


fun FragmentActivity.replaceNotAddToBackStack(containerId: Int, fragment: Fragment, tag: String?) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment, tag)
        .commit()
}

fun FragmentActivity.replace(containerId: Int, fragment: Fragment, tag: String?) {
    supportFragmentManager.beginTransaction()
        .replace(containerId, fragment, tag)
        .addToBackStack(tag)
        .commit()
}