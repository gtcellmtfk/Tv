package com.bytebyte6.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.NetworkErrorFragment
import com.bytebyte6.base.NetworkHelper
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.me.MeFragment
import com.google.android.material.navigation.NavigationView
import com.google.android.material.transition.platform.MaterialFadeThrough
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val networkHelper by inject<NetworkHelper>()

    private val drawerLayout by lazy { findViewById<DrawerLayout>(R.id.drawLayout) }

    private val navView by lazy { findViewById<NavigationView>(R.id.navView) }

    private var menuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enter = MaterialFadeThrough()
        window.enterTransition = enter

        window.allowEnterTransitionOverlap = true

        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
        }

        networkHelper.liveData().observe(this, EventObserver { connected ->
            if (!connected) {
                val fragment = NetworkErrorFragment()
                replace(fragment, NetworkErrorFragment.TAG)
            } else {
                supportFragmentManager.findFragmentByTag(NetworkErrorFragment.TAG)?.apply {
                    supportFragmentManager.popBackStack()
                }
            }
        })

        navView.setCheckedItem(R.id.nav_home)

        menuItem = navView.checkedItem

        navView.setNavigationItemSelectedListener {
            if (it == menuItem) {
                false
            } else {
                closeDrawer()
                menuItem = it
                true
            }
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {

            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerClosed(drawerView: View) {
                menuItem?.apply {
                    if (navView.checkedItem == this) {
                        return@apply
                    }
                    when (itemId) {
                        R.id.nav_home -> {
                            replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
                        }
                        R.id.nav_me -> {
                            replaceNotAddToBackStack(MeFragment(), MeFragment.TAG)
                        }
                    }
                }
            }

            override fun onDrawerOpened(drawerView: View) {

            }
        })
    }

    fun openDrawer() {
        drawerLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

}