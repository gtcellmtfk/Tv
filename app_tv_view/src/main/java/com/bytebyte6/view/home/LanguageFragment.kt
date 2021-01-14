package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.LinearSpaceDecoration
import com.bytebyte6.view.*
import com.bytebyte6.view.card.CardAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LanguageFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "ViewPagerFragment"
        fun newInstance(): LanguageFragment {
            return LanguageFragment().apply {
                arguments = Bundle().apply {
                    putInt(TAB, TAB_LANGUAGE)
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
                showVideoListFragment(view1, item.title)
            }

            recyclerView.adapter = cardAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration())

            viewModel.lang.observe(viewLifecycleOwner, Observer {
                cardAdapter.submitList(it)
            })
        }
}
