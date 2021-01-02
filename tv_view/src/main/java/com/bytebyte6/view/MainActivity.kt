package com.bytebyte6.view

import android.os.Bundle
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.NetworkErrorFragment
import com.bytebyte6.base.NetworkHelper
import com.bytebyte6.view.home.HomeFragment
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity() {

    private val networkHelper by inject<NetworkHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.main_container, HomeFragment(), HomeFragment.TAG)
//                .commit()
//        }
//
        networkHelper.liveData().observe(this, EventObserver { connected ->
            if (!connected) {
                val f = NetworkErrorFragment()
                f.setNavigationOnClickListener {
                    supportFragmentManager.popBackStack()
                }
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, f, NetworkErrorFragment.TAG)
                    .addToBackStack(HomeFragment.TAG)
                    .commit()
            } else {
                val f = supportFragmentManager.findFragmentByTag(NetworkErrorFragment.TAG)
                f?.apply {
                    supportFragmentManager.popBackStack()
                }
            }
        })
    }
}