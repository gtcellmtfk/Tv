package com.bytebyte6.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.NetworkHelper
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.NetworkErrorFragment
import com.bytebyte6.base.SimpleDrawerListener
import com.bytebyte6.base.navigationItemBackground
import com.bytebyte6.data.dao.UserDao
import com.bytebyte6.view.databinding.ActivityMainBinding
import com.bytebyte6.view.download.DownloadFragment
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.me.MeFragment
import com.bytebyte6.view.setting.SettingFragment
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    companion object {
        const val CURRENT_MENU_ITEM_ID = "CURRENT_MENU_ITEM_ID"
    }

    private val networkHelper by inject<NetworkHelper>()

    private val userDao by inject<UserDao>()

    private var current: MenuItem? = null

    private lateinit var binding: ActivityMainBinding

    private val listener = object : SimpleDrawerListener() {
        override fun onDrawerClosed(drawerView: View) {
            current?.apply {
                when (itemId) {
                    R.id.nav_home -> {
                        replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
                    }
                    R.id.nav_me -> {
                        replaceNotAddToBackStack(MeFragment(), MeFragment.TAG)
                    }
                    R.id.nav_setting -> {
                        replaceNotAddToBackStack(SettingFragment.newInstance(), SettingFragment.TAG)
                    }
                    R.id.nav_fav -> {
                        replaceNotAddToBackStack(
                            FavoriteFragment.newInstance(),
                            FavoriteFragment.TAG
                        )
                    }
                    R.id.nav_download -> {
                        replaceNotAddToBackStack(
                            DownloadFragment.newInstance(),
                            DownloadFragment.TAG
                        )
                    }
                }
                removeDrawerListener()
            }
        }
    }

    private fun addDrawerListener() {
        binding.drawLayout.addDrawerListener(listener)
    }

    private fun removeDrawerListener() {
        binding.drawLayout.removeDrawerListener(listener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceNotAddToBackStack(HomeFragment(), HomeFragment.TAG)
        }

        networkHelper.liveData().observe(this,
            EventObserver { connected ->
                if (!connected) {
                    val fragment = NetworkErrorFragment()
                    replace(fragment, NetworkErrorFragment.TAG)
                } else {
                    supportFragmentManager.findFragmentByTag(NetworkErrorFragment.TAG)?.apply {
                        supportFragmentManager.popBackStack()
                    }
                }
            })

        binding.navView.apply {

            itemBackground = navigationItemBackground(this@MainActivity)

            setNavigationItemSelectedListener { newItem ->
                if (current != newItem) {
                    current = newItem
                    addDrawerListener()
                    closeDrawer()
                    true
                } else {
                    false
                }
            }

            current = if (savedInstanceState != null) {
                val id = savedInstanceState.getInt(CURRENT_MENU_ITEM_ID)
                menu.findItem(id)
            } else {
                setCheckedItem(R.id.nav_home)
                checkedItem
            }
        }

        binding.drawLayout.apply {
            fitsSystemWindows
            setScrimColor(0)
            drawerElevation = 0f
        }

        val name = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvName)
        userDao.user().observe(this, Observer {
            name.text = it.name
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        current?.apply {
            outState.putInt(CURRENT_MENU_ITEM_ID, this.itemId)
        }
        super.onSaveInstanceState(outState)

    }

    fun selectedNavHomeMenuItem() {
        binding.navView.setCheckedItem(R.id.nav_home)
        current = binding.navView.checkedItem
    }

    fun lockDrawer() {
        binding.drawLayout.setDrawerLockMode(
            DrawerLayout.LOCK_MODE_LOCKED_CLOSED,
            GravityCompat.START
        )
    }

    fun unlockDrawer() {
        binding.drawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START)
    }

    fun openDrawer() {
        binding.drawLayout.openDrawer(GravityCompat.START)
    }

    fun closeDrawer() {
        binding.drawLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (binding.drawLayout.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}