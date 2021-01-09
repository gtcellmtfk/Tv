package com.bytebyte6.view

import android.app.Activity
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.bytebyte6.base.logd
import com.bytebyte6.view.videolist.VideoListFragment

const val KEY_ITEM = "KEY_ITEM"
const val KEY_PLAY_LIST_ID = "KEY_PLAY_LIST_ID"
const val KEY_TITLE = "KEY_TITLE"

fun Fragment.showVideoListFragment(view: View, item: String) {
    replaceWithShareElement(
        VideoListFragment.newInstance(view.transitionName, item),
        VideoListFragment.TAG,
        view
    )
}

fun Fragment.replaceWithShareElement(fragment: Fragment, tag: String?, share: View) {
    requireActivity().supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .addSharedElement(share, share.transitionName)
        .replace(R.id.main_container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

fun Fragment.replace(fragment: Fragment, tag: String?) {
    requireActivity().replace(fragment, tag)
}

fun FragmentActivity.replaceNotAddToBackStack(fragment: Fragment, tag: String?) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_container, fragment, tag)
        .commit()
}

fun FragmentActivity.replace(fragment: Fragment, tag: String?) {
    supportFragmentManager.beginTransaction()
        .replace(R.id.main_container, fragment, tag)
        .addToBackStack(tag)
        .commit()
}

fun Fragment.popBackStack() {
    this.activity?.apply {
        logd(supportFragmentManager.backStackEntryCount.toString())
        supportFragmentManager.popBackStack()
    }
}

fun Fragment.popBackStack(tag: String?) {
    this.activity?.apply {
        supportFragmentManager.popBackStack(tag, POP_BACK_STACK_INCLUSIVE)
    }
}

fun Fragment.setupToolbar(doSomeWorkBeforePop: (() -> Unit)? = null) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    toolbar.apply {
        setNavigationOnClickListener {
            doSomeWorkBeforePop?.invoke()
            popBackStack()
        }
        setNavigationIcon(R.drawable.ic_arrow_back)
    }
}

fun Fragment.setupToolbar(activity: Activity) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    toolbar.apply {
        setNavigationOnClickListener {
            activity.findViewById<DrawerLayout>(R.id.drawLayout).openDrawer(GravityCompat.START)
        }
        setNavigationIcon(R.drawable.ic_menu)
    }
}