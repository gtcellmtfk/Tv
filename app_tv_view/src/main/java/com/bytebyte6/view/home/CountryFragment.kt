package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.GridSpaceDecoration
import com.bytebyte6.view.ImageAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.showVideoListFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class CountryFragment : BaseShareFragment/*<FragmentRecyclerViewBinding>*/(R.layout.fragment_recycler_view) {

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

    override fun initBinding(view: View): FragmentRecyclerViewBinding {
        return FragmentRecyclerViewBinding.bind(view).apply {

            val imageAdapter = ImageAdapter()
            recyclerView.adapter = imageAdapter
            recyclerView.layoutManager=GridLayoutManager(view.context,2)
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            imageAdapter.setOnBind { pos, _ ->
                viewModel.searchLogo(pos)
            }

            viewModel.cs.observe(viewLifecycleOwner, Observer {
                imageAdapter.submitList(it)
            })
            imageAdapter.setOnItemClick { pos, view ->
                showVideoListFragment(view, imageAdapter.currentList[pos].transitionName)
            }
        }
    }
}