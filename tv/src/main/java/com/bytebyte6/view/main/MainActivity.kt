package com.bytebyte6.view.main

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.Observer
import com.bytebyte6.common.*
import com.bytebyte6.data.DataManager
import com.bytebyte6.view.DrawerHelper
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.ActivityMainBinding
import com.bytebyte6.view.toHome
import com.bytebyte6.view.toNetworkError
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    companion object {
        const val CURRENT_MENU_ITEM_ID = "CURRENT_MENU_ITEM_ID"
    }

    private val networkHelper by inject<NetworkHelper>()

    private val dataManager by inject<DataManager>()

    private lateinit var binding: ActivityMainBinding

    private lateinit var drawerHelper: DrawerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        drawerHelper = DrawerHelper.getInstance(this)!!

        if (savedInstanceState == null) {
            toHome()
        }

        networkHelper.liveData().observe(this,
            EventObserver { connected ->
                if (!connected) {
                    toNetworkError()
                } else {
                    supportFragmentManager.findFragmentByTag(NetworkErrorFragment.TAG)?.apply {
                        supportFragmentManager.popBackStack()
                    }
                }
            })

        binding.navView.apply {

            itemBackground = navigationItemBackground(this@MainActivity)

            setNavigationItemSelectedListener { newItem ->
                if (drawerHelper.current != newItem) {
                    drawerHelper.current = newItem
                    drawerHelper.addDrawerListener()
                    drawerHelper.closeDrawer()
                    true
                } else {
                    false
                }
            }

            drawerHelper.current = if (savedInstanceState != null) {
                val id = savedInstanceState.getInt(CURRENT_MENU_ITEM_ID)
                menu.findItem(id)
            } else {
                setCheckedItem(R.id.nav_home)
                checkedItem
            }
        }

        val name = binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvName)
        dataManager.user().observe(this, Observer {
            name.text = it.name
        })
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
        DrawerHelper.destroy()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawerHelper.isDrawerOpen()) {
            drawerHelper.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}