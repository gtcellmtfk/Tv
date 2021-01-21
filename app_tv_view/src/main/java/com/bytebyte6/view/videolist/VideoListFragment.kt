package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.bytebyte6.base.mvi.runIfNotHandled
import com.bytebyte6.base.mvi.emit
import com.bytebyte6.base_ui.*
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.library.GridSpaceDecoration
import com.bytebyte6.library.ListFragment
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
        setupToolbarArrowBack()
        viewModel.setKey(title)

        val adapter = ImageAdapter(ButtonType.FAVORITE) {
            viewModel.fav(it)
        }
        adapter.setOnItemClick { pos, _ ->
            showVideoActivity(adapter.currentList[pos].videoUrl)
        }
        adapter.setDoOnBind { pos, _ ->
            viewModel.searchLogo(pos)
        }
        adapter.setOnCurrentListChanged { _, currentList ->
            emptyBox.isVisible = currentList.isEmpty()
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(view.context, 2)
        recyclerView.addItemDecoration(GridSpaceDecoration())
        recyclerView.setHasFixedSize(true)
        recyclerView.itemAnimator=null

        viewModel.count(title).observe(viewLifecycleOwner, Observer {
            toolbar.subtitle = getString(R.string.total, it)
        })

        viewModel.tvs.observe(viewLifecycleOwner, Observer { result ->
            result.emit(
                {
                    adapter.submitList(TvFts.toTvs(it.data))
                    end = it.end
                    it.runIfNotHandled {
                        hideSwipeRefresh()
                        hideProgress()
                    }
                },
                {
                    it.runIfNotHandled {
                        showSnack(view, Message(message = it.error.message.toString()))
                        hideSwipeRefresh()
                        hideProgress()
                    }
                },
                {
                    it.runIfNotHandled {
                        showProgress()
                    }
                }
            )
        })
        viewModel.loadOnce()
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

    override fun onRefresh() {
        //not to do
    }

    override fun onViewCreated(view: View): ViewBinding? {
        return null
    }
}
