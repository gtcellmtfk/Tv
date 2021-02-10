package com.bytebyte6.view

import android.content.Intent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.bytebyte6.common.NetworkErrorFragment
import com.bytebyte6.view.download.DownloadFragment
import com.bytebyte6.view.favorite.FavoriteFragment
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.main.MainActivity
import com.bytebyte6.view.me.MeFragment
import com.bytebyte6.view.me.PlaylistFragment
import com.bytebyte6.view.player.PlayerActivity
import com.bytebyte6.view.search.SearchFragment2
import com.bytebyte6.view.setting.SettingFragment
import com.bytebyte6.view.videolist.VideoListFragment
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.material.appbar.MaterialToolbar

const val KEY_ITEM = "KEY_ITEM"
const val KEY_VIDEO_URL = "KEY_VIDEO_URL"
const val KEY_PLAY_LIST_ID = "KEY_PLAY_LIST_ID"
const val KEY_TITLE = "KEY_TITLE"
const val KEY_CACHE = "KEY_CACHE"

fun FragmentActivity.toNetworkError(){
    val fragment = NetworkErrorFragment()
    replace(fragment, NetworkErrorFragment.TAG)
}

fun Fragment.homeToVideoList(
    view: View,
    title: String
) {
    replaceWithShareElement(
        VideoListFragment.newInstance(view.transitionName, title),
        VideoListFragment.TAG,
        view
    )
}

fun Fragment.homeToSearch(toolbar: MaterialToolbar) {
    replaceWithShareElement(
        SearchFragment2.newInstance(toolbar.transitionName),
        SearchFragment2.TAG,
        toolbar
    )
}

fun Fragment.toPlayer(url: String, cache: DownloadRequest? = null) {
    startActivity(Intent(context, PlayerActivity::class.java).apply {
        putExtra(KEY_VIDEO_URL, url)
        putExtra(KEY_CACHE, cache)
    })
}

fun FragmentActivity.toHome() {
    replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
}

fun FragmentActivity.toMe() {
    replaceNotAddToBackStack(MeFragment(), MeFragment.TAG)
}

fun FragmentActivity.toSetting() {
    replaceNotAddToBackStack(SettingFragment.newInstance(), SettingFragment.TAG)
}

fun FragmentActivity.toFav() {
    replaceNotAddToBackStack(
        FavoriteFragment.newInstance(),
        FavoriteFragment.TAG
    )
}

fun FragmentActivity.toDownload() {
    replaceNotAddToBackStack(
        DownloadFragment.newInstance(),
        DownloadFragment.TAG
    )
}

fun Fragment.meToPlaylist(
    playlistId: Long,
    title: String,
    transitionName: String,
    itemView: View
) {
    replaceWithShareElement(
        PlaylistFragment.newInstance(
            playlistId, title, transitionName
        ),
        PlaylistFragment.TAG,
        itemView
    )
}

fun Fragment.meToPlaylist(
    playlistId: Long,
    title: String,
    transitionName: String
) {
    replace(
        PlaylistFragment.newInstance(
            playlistId, title, transitionName
        ),
        PlaylistFragment.TAG
    )
}

fun Fragment.setupOnBackPressedDispatcherBackToHome() {
    val activity = requireActivity()
    if (activity is MainActivity){
        activity.onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity.replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
                    activity.selectedNavHomeMenuItem()
                }
            }
        )
    }
}

 fun Fragment.replaceWithShareElement(
    fragment: Fragment,
    tag: String?,
    share: View
) {
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
