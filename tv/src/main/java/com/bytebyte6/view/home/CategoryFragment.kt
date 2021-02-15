package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.viewmodel.HomeViewModel
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.common.dp8
import com.bytebyte6.utils.LinearSpaceDecoration
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_CATEGORY
import com.bytebyte6.view.adapter.CategoryAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.homeToVideoList
import org.koin.android.viewmodel.ext.android.getViewModel

class CategoryFragment :
    BaseShareFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "ViewPagerFragment"
        fun newInstance(): CategoryFragment {
            return CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt(TAB, TAB_CATEGORY)
                }
            }
        }
    }

    private val viewModel: HomeViewModel by lazy {
        requireParentFragment().getViewModel<HomeViewModel>()
    }

    override fun initViewBinding(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryAdapter = CategoryAdapter()
        categoryAdapter.onItemClick = { pos, itemView: View ->
            val item = categoryAdapter.currentList[pos]
            homeToVideoList(itemView, item.category)
        }

        recyclerView = binding?.recyclerView
        binding?.apply {
            recyclerView.adapter = categoryAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration(top = dp8))
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
        }

        viewModel.category.observe(viewLifecycleOwner, Observer {
            categoryAdapter.submitList(it)
        })
    }
}
