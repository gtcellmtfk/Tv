package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.LinearSpaceDecoration
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_CATEGORY
import com.bytebyte6.view.card.CardAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.showVideoListFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class CategoryFragment :
    BaseShareFragment/*<FragmentRecyclerViewBinding>*/(R.layout.fragment_recycler_view) {

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

    override fun initBinding(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view).apply {

            val cardAdapter = CardAdapter()
            cardAdapter.setOnItemClick { pos, view1 ->
                val item = cardAdapter.currentList[pos]
                showVideoListFragment(view1, item.transitionName)
            }

            recyclerView.adapter = cardAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration())
            recyclerView.setHasFixedSize(true)

            viewModel.category.observe(viewLifecycleOwner, Observer {
                cardAdapter.submitList(it)
            })
        }
}
