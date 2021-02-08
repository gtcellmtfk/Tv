package com.bytebyte6.view.search

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.viewmodel.SearchViewModel
import com.bytebyte6.common.*
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.doSomethingOnIdle
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.TvAdapter
import com.bytebyte6.view.databinding.FragmentSearchBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : BaseShareFragment<FragmentSearchBinding>(R.layout.fragment_search){

    companion object {
        const val TAG = "SearchFragment"
        fun newInstance(transName: String): SearchFragment {
            return SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TRANS_NAME, transName)
                }
            }
        }
    }

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnSharedElementReturnTransitionEnd {
            clearRecyclerView()
        }
    }

    override fun initViewBinding(view: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarArrowBack { KeyboardUtils.hideSoftInput(requireActivity()) }
        val adapter = TvAdapter(
            ButtonType.FAVORITE,
            object : ButtonClickListener {
                override fun onClick(position: Int,tv: Tv) {
                    viewModel.fav(position)
                }
            }).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].url)
            }
            onCurrentListChanged = { _, currentList ->
                binding?.lavEmpty?.isVisible = currentList.isEmpty()
            }
        }
        imageClearHelper = adapter
        recyclerView = binding?.recyclerView

        binding?.apply {
            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.search(text)
                btnClear.isVisible = !text.isNullOrEmpty()
            }
            btnClear.setOnClickListener {
                etSearch.setText("")
            }
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recyclerView.doSomethingOnIdle { first, last ->
                viewModel.searchLogo(first, last)
            }
            recyclerView.doOnPreDraw {
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                viewModel.searchLogo(
                    layoutManager.findFirstVisibleItemPosition(),
                    layoutManager.findLastVisibleItemPosition()
                )
            }
            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
        viewModel.favoriteResult.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(success = {
                adapter.currentList[it.data.pos].favorite=it.data.tv.favorite
                adapter.notifyItemChanged(it.data.pos)
            })
        })
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}