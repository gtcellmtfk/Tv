package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.viewmodel.HomeViewModel
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.utils.LinearSpaceDecoration
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.adapter.CardAdapter
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.homeToVideoList
import org.koin.android.viewmodel.ext.android.getViewModel

class LanguageFragment : BaseShareFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

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

    override fun initViewBinding(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cardAdapter = CardAdapter()
        cardAdapter.onItemClick= { pos, itemView ->
            val item = cardAdapter.currentList[pos]
            homeToVideoList(itemView, item.transitionName)
        }
        recyclerView=binding?.recyclerView
        binding?.apply {
            recyclerView.adapter = cardAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration())
            recyclerView.setHasFixedSize(true)
        }

        viewModel.lang.observe(viewLifecycleOwner, Observer {
            cardAdapter.submitList(it)
        })
    }
}
