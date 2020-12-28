package com.bytebyte6.view

import android.os.Bundle
import com.bytebyte6.base.BaseActivity
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.IpTvViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<IpTvViewModel>()

    override fun initViewModel(): BaseViewModel? {
        return viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)

        supportFragmentManager.beginTransaction()
            .add(R.id.container,HomeFragment(),HomeFragment.TAG)
            .commit()

        initShowLoading(findViewById(R.id.progressBar))

    }
}