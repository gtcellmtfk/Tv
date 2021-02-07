package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.doSomethingOnIdle
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.CountryAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.homeToVideoList
import com.bytebyte6.viewmodel.HomeViewModel
import org.koin.android.viewmodel.ext.android.getViewModel

class CountryFragment :
    BaseShareFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "CountryFragment"
        fun newInstance(): CountryFragment {
            return CountryFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val viewModel: HomeViewModel by lazy {
        requireParentFragment().getViewModel<HomeViewModel>()
    }

    override fun initViewBinding(view: View): FragmentRecyclerViewBinding {
        return FragmentRecyclerViewBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageAdapter = CountryAdapter().apply {
            onItemClick = { pos, itemView: View ->
                homeToVideoList(
                    itemView,
                    currentList[pos].name
                )
            }
        }
        recyclerView = binding?.recyclerView
        imageClearHelper = imageAdapter
        binding?.apply {
            recyclerView.adapter = imageAdapter
            val gridLayoutManager = GridLayoutManager(view.context, 2)
            recyclerView.layoutManager = gridLayoutManager
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.doSomethingOnIdle { first, last ->
                viewModel.searchLogo(first, last)
            }
            recyclerView.doOnPreDraw {
                viewModel.searchLogo(
                    gridLayoutManager.findFirstVisibleItemPosition(),
                    gridLayoutManager.findLastVisibleItemPosition()
                )
            }
        }

        viewModel.cs.observe(viewLifecycleOwner, Observer {
            imageAdapter.submitList(it)
        })
    }
}