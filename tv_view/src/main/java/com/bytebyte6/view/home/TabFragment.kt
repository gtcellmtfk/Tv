package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.databinding.FragmentTabBinding
import com.bytebyte6.view.replaceWithShareElement
import com.bytebyte6.view.videolist.VideoListFragment
import com.google.android.material.transition.Hold
import org.koin.android.viewmodel.ext.android.sharedViewModel

class TabFragment :
    BaseFragment<FragmentTabBinding>(R.layout.fragment_tab) {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    private val viewModel: HomeViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun initBinding(view: View): FragmentTabBinding =
        FragmentTabBinding.bind(view).apply {
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            val tab = requireArguments().getInt(TAB)

            val singleLineAdapter = StringAdapter()

            singleLineAdapter.setOnItemClick { pos, view1 ->
                val item = singleLineAdapter.currentList[pos]
                showVideoListFragment(view1,item)
            }

            recyclerView.adapter = singleLineAdapter

            viewModel.listLiveData(tab)?.observe(viewLifecycleOwner, Observer {
                singleLineAdapter.submitList(it)
            })
        }

    private fun showVideoListFragment(view: View, item: String) {
        replaceWithShareElement(
            VideoListFragment.newInstance(view.transitionName,item),
            VideoListFragment.TAG,
            view
        )
    }
}
