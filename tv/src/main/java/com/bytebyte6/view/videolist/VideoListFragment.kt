package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.common.*
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.KEY_TITLE
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.ImageAdapter
import com.bytebyte6.view.setupToolbarArrowBack
import com.bytebyte6.view.toPlayer
import com.bytebyte6.viewmodel.VideoListViewModel
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

    private val buttonClickListener = object :
        ButtonClickListener {
        override fun onClick(position: Int) {
            viewModel.fav(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnSharedElementReturnTransitionEnd {
            clearRecyclerView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ImageAdapter(
            ButtonType.FAVORITE,
            buttonClickListener
        ).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].videoUrl)
            }
            doOnBind = { pos, _: View ->
                if (recyclerView!!.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.searchLogo(pos)
                }
            }
            onCurrentListChanged = { _, currentList ->
                binding?.emptyBox?.isVisible = currentList.isEmpty()
            }
        }
        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()

        imageClearHelper = adapter

        val title = requireArguments().getString(KEY_TITLE)!!
        setupToolbarArrowBack(title)
        viewModel.setKey(title)

        binding?.run {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = GridLayoutManager(view.context, 2)
            recyclerview.addItemDecoration(GridSpaceDecoration())
            recyclerview.setHasFixedSize(true)
            recyclerview.itemAnimator = null
        }

        viewModel.count(title).observe(viewLifecycleOwner, Observer {
            binding?.appbar?.toolbar?.subtitle = getString(R.string.total, it)
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
                    showSnack(view, Message(message = it.error.message.toString()))
                    hideSwipeRefresh()
                    hideProgress()
                },
                {
                    showProgress()
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
}
