package com.bytebyte6.view

import android.os.Bundle
import android.view.View
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.HomeViewModel
import com.bytebyte6.view.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    private val viewModel: HomeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            viewPager.adapter = ViewPagerAdapter(this@HomeFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> tab.setText(R.string.home_country)
                    1 -> tab.setText(R.string.home_language)
                    2 -> tab.setText(R.string.home_type)
                }
            }.attach()
        }
    }

    override fun initBinding(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view)
    }

    override fun initViewModel(): BaseViewModel {
        return viewModel
    }
}