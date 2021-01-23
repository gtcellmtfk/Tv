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
import com.bytebyte6.base_ui.KEY_TRANS_NAME
import com.bytebyte6.library.GridSpaceDecoration
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentSearchBinding
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : BaseShareFragment<FragmentSearchBinding>(R.layout.fragment_search) {

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

    override fun initViewBinding(view: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarArrowBack { KeyboardUtils.hideSoftInput(requireActivity()) }
        val adapter = ImageAdapter(ButtonType.FAVORITE,object : ButtonClickListener{
            override fun onClick(position: Int, view: View) {
                viewModel.fav(position)
            }
        })
        adapter.onItemClick= { pos , _: View->
            showVideoActivity(adapter.currentList[pos].videoUrl)
        }
        adapter.doOnBind=  { pos, _: View ->
            viewModel.searchLogo(pos)
        }
        adapter.onCurrentListChanged= { _, currentList ->
            binding?.lavEmpty?.isVisible = currentList.isEmpty()
        }
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
            recyclerView.itemAnimator=null

            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
        viewModel.favorite.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(success = {
                adapter.notifyItemChanged(it.data.pos)
            })
        })
        viewModel.logoSearch.observe(viewLifecycleOwner, Observer {
            it.emitIfNotHandled(success = {
                viewModel.search(binding?.etSearch?.text)
            })
        })
        viewModel.tvs.observe(viewLifecycleOwner, Observer {
            it.isSuccess()?.apply {
                adapter.submitList(this)
            }
        })
    }
}