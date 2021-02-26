package com.bytebyte6.view

import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import com.bytebyte6.common.SimpleDrawerListener

class DrawerHelper constructor(
    private val drawerLayout: DrawerLayout
) {

    init {
        drawerLayout.apply {
            setScrimColor(0)
            drawerElevation = 0f
        }
    }

    var current: MenuItem? = null

    fun addDrawerListener(listener: SimpleDrawerListener) {
        drawerLayout.addDrawerListener(listener)
    }

    fun removeDrawerListener(listener: SimpleDrawerListener) {
        drawerLayout.removeDrawerListener(listener)
    }

    fun lockDrawer() {
        drawerLayout.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
            GravityCompat.START
        )
    }

    fun unlockDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START)
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    fun isDrawerOpen() = drawerLayout.isDrawerOpen(GravityCompat.START)
}