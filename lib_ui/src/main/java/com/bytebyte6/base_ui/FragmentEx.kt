package com.bytebyte6.base_ui

import android.app.Activity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun Fragment.replaceWithShareElement(
    containerId: Int,
    fragment: Fragment,
    tag: String?,
    share: View
) {
    requireActivity().supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .addSharedElement(share, share.transitionName)
        .replace(containerId, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

/**
 * 返回上一层
 */
fun Fragment.popBackStack() {
    this.activity?.apply {
        supportFragmentManager.popBackStack()
    }
}

/**
 * 返回指定片段
 */
fun Fragment.popBackStack(tag: String?) {
    this.activity?.apply {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

/**
 * 设置导航键为打开抽屉样式
 */
fun Fragment.setupToolbarDrawLayout(activity: Activity, drawLayoutId: Int = 0) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    toolbar.apply {
        setNavigationOnClickListener {
            if (drawLayoutId != 0) {
                activity.findViewById<DrawerLayout>(drawLayoutId).openDrawer(GravityCompat.START)
            }
        }
        setNavigationIcon(R.drawable.ic_menu)
    }
}

/**
 * 设置导航键为返回样式
 */
fun Fragment.setupToolbarPopUp(doSomeWorkWhenPopUp: (() -> Unit)? = null) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    toolbar.apply {
        setNavigationOnClickListener {
            doSomeWorkWhenPopUp?.invoke()
            popBackStack()
        }
        setNavigationIcon(R.drawable.ic_arrow_back)
    }
}