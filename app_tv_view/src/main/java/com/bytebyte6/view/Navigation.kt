package com.bytebyte6.view

import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import androidx.transition.Transition
import com.bytebyte6.base_ui.DefaultTransitionListener
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.player.PlayerActivity
import com.bytebyte6.view.videolist.VideoListFragment
import com.google.android.exoplayer2.offline.DownloadRequest

const val KEY_ITEM = "KEY_ITEM"
const val KEY_VIDEO_URL = "KEY_VIDEO_URL"
const val KEY_PLAY_LIST_ID = "KEY_PLAY_LIST_ID"
const val KEY_TITLE = "KEY_TITLE"
const val KEY_CACHE = "KEY_CACHE"

fun Fragment.setupOnBackPressedDispatcherBackToHome() {
    val mainActivity = requireActivity() as MainActivity
    mainActivity.onBackPressedDispatcher.addCallback(
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                remove()
                mainActivity.replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
                mainActivity.selectedNavHomeMenuItem()
            }
        }
    )
}

fun Fragment.showVideoActivity(url: String, cache: DownloadRequest? = null) {
    startActivity(Intent(context, PlayerActivity::class.java).apply {
        putExtra(KEY_VIDEO_URL, url)
        putExtra(KEY_CACHE, cache)
    })
}

fun Fragment.showVideoListFragment(
    view: View,
    title: String,
    doOnTransitionEnd: (() -> Unit)? = null
) {
    replaceWithShareElement(
        VideoListFragment.newInstance(view.transitionName, title),
        VideoListFragment.TAG,
        view, doOnTransitionEnd
    )
}

fun Fragment.replaceWithShareElement(
    fragment: Fragment,
    tag: String?,
    share: View,
    doOnTransitionEnd: (() -> Unit)? = null
) {
    val transition = fragment.sharedElementEnterTransition as Transition
    transition.addListener(object : DefaultTransitionListener() {
        override fun onTransitionEnd(transition: Transition) {
            transition.removeListener(this)
            doOnTransitionEnd?.invoke()
        }
    })
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
    this.activity?.supportFragmentManager?.popBackStack()
}

fun Fragment.popBackStack(tag: String?) {
    this.activity?.supportFragmentManager?.popBackStack(tag, POP_BACK_STACK_INCLUSIVE)
}

fun Fragment.setupToolbarArrowBack(
    title: String? = null,
    subTitle: String? = null,
    doSomeWorkBeforePop: (() -> Unit)? = null
) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    if (title != null) {
        toolbar.title = title
    }
    if (subTitle != null) {
        toolbar.subtitle = subTitle
    }
    toolbar.setNavigationOnClickListener {
        doSomeWorkBeforePop?.invoke()
        popBackStack()
    }
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    val mainActivity = requireActivity() as MainActivity
    mainActivity.lockDrawer()
}

fun Fragment.setupToolbarMenuMode(title: String? = null, subTitle: String? = null) {
    val toolbar = requireView().findViewById<Toolbar>(R.id.toolbar)
    val mainActivity = requireActivity() as MainActivity
    toolbar.let {
        it.setNavigationOnClickListener {
            mainActivity.openDrawer()
        }
        it.setNavigationIcon(R.drawable.ic_menu)
        title?.run {
            it.title = this
        }
        subTitle?.run {
            it.subtitle = this
        }
    }
    mainActivity.unlockDrawer()
}