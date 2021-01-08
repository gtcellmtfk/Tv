package com.bytebyte6.view.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.KeyboardUtils
import com.bytebyte6.base.mvi.isSuccess
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.view.KEY_TRANS_NAME
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentSearchBinding
import com.bytebyte6.view.setupToolbar
import com.bytebyte6.view.video.VideoActivity
import com.bytebyte6.view.video.VideoAdapter
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

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
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun initBinding(view: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(view).apply {

            view.transitionName = requireArguments().getString(KEY_TRANS_NAME)
            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            setupToolbar { KeyboardUtils.hideSoftInput(requireActivity()) }

            etSearch.doOnTextChanged { text, _, _, _ ->
                viewModel.search(text)
            }

            val adapter = VideoAdapter()
            adapter.setOnItemClick { pos, _ ->
                startActivity(Intent(context, VideoActivity::class.java).apply {
                    putExtra(Tv.TAG, adapter.currentList[pos])
                })
            }

            recyclerView.adapter = adapter
            viewModel.tvs.observe(viewLifecycleOwner, EventObserver {
                it.isSuccess()?.apply {
                    adapter.submitList(this)
                    lavEmpty.isVisible = isEmpty()
                }
            })

            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
    }
}