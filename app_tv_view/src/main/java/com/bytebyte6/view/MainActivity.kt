package com.bytebyte6.view

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import com.bytebyte6.base.*
import com.bytebyte6.base_ui.BaseActivity
import com.bytebyte6.base_ui.NetworkErrorFragment
import com.bytebyte6.base_ui.SimpleDrawerListener
import com.bytebyte6.view.databinding.ActivityMainBinding
import com.bytebyte6.view.home.HomeFragment
import com.bytebyte6.view.me.MeFragment
import com.google.android.material.transition.platform.MaterialFadeThrough
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val networkHelper by inject<NetworkHelper>()

    private var current: MenuItem? = null

    private val viewModel by viewModel<LauncherViewModel>()

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

        val enter = MaterialFadeThrough()

        window.enterTransition = enter

        window.allowEnterTransitionOverlap = true

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

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

        binding.navView.apply {

//            itemBackground = navigationItemBackground(this@MainActivity)

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

            setCheckedItem(R.id.nav_home)

            current = checkedItem

        }

        binding.drawLayout.apply {
            setScrimColor(0)
            drawerElevation = 0f
        }

        viewModel.init()
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