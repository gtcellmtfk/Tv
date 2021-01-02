package com.bytebyte6.view.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.base.KeyboardUtils
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentSearchBinding
import com.bytebyte6.view.video.VideoActivity
import com.bytebyte6.view.video.VideoAdapter
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    companion object {
        const val TAG = "SearchFragment"
    }

    private val viewModel: SearchViewModel by sharedViewModel()

    private val args by navArgs<SearchFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? {
        return viewModel
    }

    override fun initBinding(view: View): FragmentSearchBinding {
        return FragmentSearchBinding.bind(view).apply {
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
            view.transitionName = args.transName
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        binding?.apply {

            etSearch.doOnTextChanged { text, start, before, count ->
                viewModel.search(text)
            }

            val adapter = VideoAdapter()
            adapter.setOnItemClick { pos, _ ->
                startActivity(Intent(context, VideoActivity::class.java).apply {
                    putExtra(IpTv.TAG, adapter.currentList[pos])
                })
            }

            recyclerView.adapter = adapter
            viewModel.searchLiveData().observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                lavEmpty.isVisible = it.isEmpty()
            })

            KeyboardUtils.showSoftInput(etSearch, requireContext())
        }
    }

    override fun onDestroyView() {
        KeyboardUtils.hideSoftInput(requireActivity())
        super.onDestroyView()
    }
}