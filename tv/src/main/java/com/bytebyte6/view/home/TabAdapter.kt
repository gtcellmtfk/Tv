package com.bytebyte6.view.home

import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bytebyte6.base.BaseShareFragment

class TabAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment.childFragmentManager, fragment.viewLifecycleOwner.lifecycle) {

    val fs = mutableListOf(
        CountryFragment.newInstance(),
        LanguageFragment.newInstance(),
        CategoryFragment.newInstance()
    )

    override fun getItemCount(): Int {
        return fs.size
    }

    override fun createFragment(position: Int): Fragment {
       return fs[position]
    }
}