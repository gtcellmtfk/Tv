package com.bytebyte6.view.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.data.entity.User
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.ActivityMainBinding
import com.bytebyte6.view.download.DownloadServicePro
import com.bytebyte6.view.launcher.LauncherActivity
import com.google.android.material.navigation.NavigationView
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val CURRENT_MENU_ITEM_ID = "CURRENT_MENU_ITEM_ID"
    }

    private val networkHelper by inject<NetworkHelper>()

    private val dataManager by inject<DataManager>()

    private lateinit var binding: ActivityMainBinding

    lateinit var drawerHelper: DrawerHelper

    private val listener = object : SimpleDrawerListener() {
        override fun onDrawerClosed(drawerView: View) {
            drawerHelper.current?.let {
                when (it.itemId) {
                    R.id.nav_home -> toHome()
                    R.id.nav_me -> toMe()
                    R.id.nav_setting -> toSetting()
                    R.id.nav_fav -> toFav()
                    R.id.nav_download -> toDownload()
                    R.id.nav_about -> toAbout()
                }
                drawerHelper.removeDrawerListener(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkHelper.registerNetworkCallback()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        drawerHelper = DrawerHelper(binding.drawLayout)

        if (savedInstanceState == null) {
            toHome()
        }

        var count = 0
        binding.tvCn.setOnClickListener {
            if (count >= 10) {
                throw NoMoreData
            }
            count++
        }

        networkHelper.networkConnected.observe(this, Observer { connected ->
            if (!connected) {
                toNetworkError()
            } else {
                val fragment =
                    supportFragmentManager.findFragmentByTag(NetworkErrorFragment.TAG)
                fragment?.requireActivity()?.supportFragmentManager?.popBackStack()
            }
        })

        binding.navView.apply {

            itemBackground = navigationItemBackground(this@MainActivity)

            setNavigationItemSelectedListener(this@MainActivity)

            drawerHelper.current = if (savedInstanceState != null) {
                val id = savedInstanceState.getInt(CURRENT_MENU_ITEM_ID)
                menu.findItem(id)
            } else {
                setCheckedItem(R.id.nav_home)
                checkedItem
            }
        }

        val name = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvName)
        dataManager.user().observe(this, Observer { user1 ->
            name.text = user1.name
            networkHelper.networkType.observe(this, getNetworkTypeObs(user1))
        })
    }

    private fun getNetworkTypeObs(user: User): Observer<NetworkHelper.NetworkType> {
        return Observer { type ->
            when (type) {
                NetworkHelper.NetworkType.WIFI -> {
                    DownloadServicePro.resumeDownloads(this)
                }
                NetworkHelper.NetworkType.MOBILE -> {
                    if (user.downloadOnlyOnWifi) {
                        DownloadServicePro.pauseDownloads(this)
                    }
                }
                else -> Unit
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        drawerHelper.current?.apply {
            outState.putInt(CURRENT_MENU_ITEM_ID, this.itemId)
        }
        super.onSaveInstanceState(outState)

    }

    fun selectedNavHomeMenuItem() {
        binding.navView.setCheckedItem(R.id.nav_home)
        drawerHelper.current = binding.navView.checkedItem
    }

    override fun onDestroy() {
        networkHelper.unregisterNetworkCallback()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawerHelper.isDrawerOpen()) {
            drawerHelper.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return if (drawerHelper.current != item) {
            drawerHelper.current = item
            drawerHelper.addDrawerListener(listener)
            drawerHelper.closeDrawer()
            true
        } else {
            false
        }
    }
}