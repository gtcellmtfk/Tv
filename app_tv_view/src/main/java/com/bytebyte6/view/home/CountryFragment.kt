package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base.BaseShareFragment
import com.bytebyte6.library.GridSpaceDecoration
import com.bytebyte6.view.ImageAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.homeToVideoList
import org.koin.android.viewmodel.ext.android.getViewModel

class CountryFragment :
    BaseShareFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "CountryFragment"
        fun newInstance(): CountryFragment {
            return CountryFragment().apply {
                arguments = Bundle().apply {

                }
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
        val imageAdapter = ImageAdapter()
        imageAdapter.doOnBind = { pos: Int, _: View ->
            viewModel.searchLogo(pos)
        }
        imageAdapter.onItemClick = { pos, itemView: View ->
            homeToVideoList(
                itemView,
                imageAdapter.currentList[pos].transitionName
            )
        }

        binding?.apply {
            recyclerView.adapter = imageAdapter
            recyclerView.layoutManager = GridLayoutManager(view.context, 2)
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
        }

        viewModel.cs.observe(viewLifecycleOwner, Observer {
            imageAdapter.submitList(it)
        })
    }
}