package com.bytebyte6.view

import android.view.View
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.HomeViewModel
import com.bytebyte6.view.databinding.FragmentViewPagerBinding
import org.koin.android.viewmodel.ext.android.viewModel

class ViewPagerFragment : BaseFragment<FragmentViewPagerBinding>(R.layout.fragment_view_pager){

    private val viewModel: HomeViewModel by viewModel()

    override fun initViewModel(): BaseViewModel {
        return viewModel
    }

    override fun initBinding(view: View): FragmentViewPagerBinding {
        return FragmentViewPagerBinding.bind(view)
    }
}