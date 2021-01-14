package com.bytebyte6.view.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class TabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                CountryFragment.newInstance()
            }
            1 -> {
                LanguageFragment.newInstance()
            }
            else -> {
                CategoryFragment.newInstance()
            }
        }
    }
}