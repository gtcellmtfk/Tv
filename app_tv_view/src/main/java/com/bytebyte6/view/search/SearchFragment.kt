package com.bytebyte6.view.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.bytebyte6.base.KeyboardUtils
import com.bytebyte6.base.mvi.emitIfNotHandled
import com.bytebyte6.base.mvi.isSuccess
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.GridSpaceDecoration
import com.bytebyte6.base_ui.KEY_TRANS_NAME
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentSearchBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : BaseShareFragment/*<FragmentSearchBinding>*/(R.layout.fragment_search) {

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

    override fun initBinding(view: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(view).apply {

            setupToolbarArrowBack { KeyboardUtils.hideSoftInput(requireActivity()) }

            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.search(text)
                btnClear.isVisible = !text.isNullOrEmpty()
            }
            btnClear.setOnClickListener {
                etSearch.setText("")
            }

            val adapter = ImageAdapter(ButtonType.FAVORITE) {
                viewModel.fav(it)
            }
            adapter.setOnItemClick { pos, _ ->
                showVideoActivity(adapter.currentList[pos].videoUrl)
            }
            adapter.setOnBind { pos, _ ->
                viewModel.searchLogo(pos)
            }
            adapter.setOnCurrentListChanged { _, currentList ->
                lavEmpty.isVisible = currentList.isEmpty()
            }

            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)

            viewModel.favorite.observe(viewLifecycleOwner, Observer { result ->
                result.emitIfNotHandled(success = {
//                    viewModel.search(etSearch.text)
//                    adapter.currentList[it.data.pos].favorite = it.data.tv.favorite
                    adapter.notifyItemChanged(it.data.pos)
                })
            })

            viewModel.logoSearch.observe(viewLifecycleOwner, Observer {
                it.emitIfNotHandled(success = {
                    viewModel.search(etSearch.text)
                })
            })

            viewModel.tvs.observe(viewLifecycleOwner, Observer {
                it.isSuccess()?.apply {
                    adapter.submitList(this)
                }
            })

            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
    }
}