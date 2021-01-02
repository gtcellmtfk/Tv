package com.bytebyte6.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytebyte6.view.TAB

class ViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return ViewPagerFragment().apply {
            arguments= Bundle().apply { putInt(TAB,position) }
        }
    }

}