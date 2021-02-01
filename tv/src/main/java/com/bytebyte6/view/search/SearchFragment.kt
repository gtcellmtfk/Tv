package com.bytebyte6.view.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.bytebyte6.viewmodel.SearchViewModel
import com.bytebyte6.common.*
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.ImageAdapter
import com.bytebyte6.view.databinding.FragmentSearchBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : BaseShareFragment<FragmentSearchBinding>(R.layout.fragment_search),
    Observer<Result<*>> {

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
        val adapter = ImageAdapter(
            ButtonType.FAVORITE,
            object : ButtonClickListener {
                override fun onClick(position: Int) {
                    viewModel.fav(position)
                }
            }).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].videoUrl)
            }
            doOnBind = { pos, _: View ->
                viewModel.searchLogo(pos)
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

            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
        viewModel.favoriteResult.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(success = {
                adapter.notifyItemChanged(it.data.pos)
            })
        })
        viewModel.logoUrlSearchResult.observe(viewLifecycleOwner, Observer {
            it.emitIfNotHandled(success = {
                viewModel.search(binding?.etSearch?.text)
            })
        })
        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            it.isSuccess()?.apply {
                adapter.submitList(this)
            }
        })
        viewModel.searchResult.observe(viewLifecycleOwner, this)
        viewModel.logoUrlSearchResult.observe(viewLifecycleOwner, this)
        viewModel.favoriteResult.observe(viewLifecycleOwner, this)
    }

    override fun onChanged(t: Result<*>) {
        t.emitIfNotHandled(
            {
                logd("Hide Loading")
            }, {
                logd("Hide Loading And show Error")
            }, {
                logd("Show Loading")
            }
        )
    }
}