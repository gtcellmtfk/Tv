package com.bytebyte6.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytebyte6.base.logd

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment.childFragmentManager,fragment.viewLifecycleOwner.lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                logd("CountryFragment")
                CountryFragment.newInstance()
            }
            1 -> {
                logd("LanguageFragment")
                LanguageFragment.newInstance()
            }
            else -> {
                logd("CategoryFragment")
                CategoryFragment.newInstance()
            }
        }
    }
}