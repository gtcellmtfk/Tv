package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.library.LinearSpaceDecoration
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.card.CardAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.showVideoListFragment
import org.koin.android.viewmodel.ext.android.getViewModel

class LanguageFragment : BaseShareFragment(R.layout.fragment_recycler_view) {

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

    override fun onViewCreated(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view).apply {

            val cardAdapter = CardAdapter()

            cardAdapter.setOnItemClick { pos, view1 ->
                val item = cardAdapter.currentList[pos]
                showVideoListFragment(view1, item.transitionName)
            }

            recyclerView.adapter = cardAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration())
            recyclerView.setHasFixedSize(true)

            viewModel.lang.observe(viewLifecycleOwner, Observer {
                cardAdapter.submitList(it)
            })
        }
}
