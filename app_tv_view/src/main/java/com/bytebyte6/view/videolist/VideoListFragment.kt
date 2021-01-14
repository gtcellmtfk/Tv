package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.doSomethingIfNotHandled
import com.bytebyte6.base_ui.*
import com.bytebyte6.base_ui.databinding.FragmentListBinding
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import org.koin.android.viewmodel.ext.android.viewModel

class VideoListFragment : ListFragment() {

    companion object {
        const val TAG = "VideoListFragment"
        fun newInstance(transName: String, title: String): VideoListFragment {
            return VideoListFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TRANS_NAME, transName)
                    putString(KEY_TITLE, title)
                }
            }
        }
    }

    private val viewModel: VideoListViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()
        val title = requireArguments().getString(KEY_TITLE)!!
        toolbar.title = title
        setupToolbar()

        val adapter = ImageAdapter()
        adapter.setOnItemClick { pos, _ ->
            showVideoFragment(adapter.currentList[pos].videoUrl)
        }
        adapter.setOnBind { pos, _ ->
            viewModel.searchLogo(pos)
        }
        adapter.setOnCurrentListChanged { _, currentList ->
            emptyBox.isVisible = currentList.isEmpty()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.addItemDecoration(GridSpaceDecoration())

        viewModel.setKey(title)

        viewModel.count(title).observe(viewLifecycleOwner, Observer {
            toolbar.subtitle = getString(R.string.total, it)
        })

        viewModel.tvs.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    adapter.submitList(TvFts.toIpTvs(it.data))
                    end = it.end
                    it.doSomethingIfNotHandled {
                        hideSwipeRefresh()
                        hideProgress()
                    }
                }
                is Result.Error -> {
                    it.doSomethingIfNotHandled {
                        showSnack(view, Message(message = it.error.message.toString()))
                        hideSwipeRefresh()
                        hideProgress()
                    }
                }
                is Result.Loading -> {
                    it.doSomethingIfNotHandled {
                        showProgress()
                    }
                }
            }
        })
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

    override fun onRefresh() {
        //not to do
    }

    override fun initBinding(view: View): FragmentListBinding? {
        return null
    }
}
