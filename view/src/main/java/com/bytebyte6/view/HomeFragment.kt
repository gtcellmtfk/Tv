package com.bytebyte6.view

import android.os.Bundle
import android.view.View
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.TAB_CATEGORY
import com.bytebyte6.logic.TAB_COUNTRY
import com.bytebyte6.logic.TAB_LANGUAGE
import com.bytebyte6.view.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    companion object {
        const val TAG = "HomeFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewPager.adapter = ViewPagerAdapter(this@HomeFragment)
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    TAB_COUNTRY -> tab.setText(R.string.home_country)
                    TAB_LANGUAGE -> tab.setText(R.string.home_language)
                    TAB_CATEGORY -> tab.setText(R.string.home_type)
                }
            }.attach()
        }
    }

    override fun initBinding(view: View): FragmentHomeBinding {
        return FragmentHomeBinding.bind(view)
    }

    override fun initViewModel(): BaseViewModel? {
        return null
    }
}