package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TvViewModel
import com.bytebyte6.view.databinding.FragmentTabBinding
import com.bytebyte6.view.replaceWithShareElement
import com.bytebyte6.view.video.VideoListFragment
import com.google.android.material.transition.Hold
import org.koin.android.viewmodel.ext.android.sharedViewModel

class TabFragment :
    BaseFragment<FragmentTabBinding>(R.layout.fragment_tab) {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    private val viewModel: TvViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? = viewModel

    override fun initBinding(view: View): FragmentTabBinding =
        FragmentTabBinding.bind(view).apply {
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            val tab = requireArguments().getInt(TAB)

            val singleLineAdapter = StringAdapter()

            singleLineAdapter.setOnItemClick { pos, view1 ->
                val item = singleLineAdapter.currentList[pos]
                viewModel.tab = tab
                viewModel.clickItem = item
                showVideoListFragment(view1)
            }

            recyclerView.adapter = singleLineAdapter

            viewModel.listLiveData(tab)?.observe(viewLifecycleOwner, Observer {
                singleLineAdapter.submitList(it)
            })
        }

    private fun showVideoListFragment(view: View) {
        replaceWithShareElement(
            VideoListFragment.newInstance(view.transitionName),
            VideoListFragment.TAG,
            view
        )
    }
}
