package com.bytebyte6.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_CATEGORY
import com.bytebyte6.view.TAB_LANGUAGE

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