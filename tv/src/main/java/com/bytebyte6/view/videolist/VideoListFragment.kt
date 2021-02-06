package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.common.*
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.KEY_TITLE
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.TvAdapter
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
        override fun onClick(position: Int, tv: Tv) {
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
        val adapter = TvAdapter(
            ButtonType.FAVORITE,
            buttonClickListener
        ).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].url)
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
            val gridLayoutManager = GridLayoutManager(view.context, 2)
            recyclerview.layoutManager = gridLayoutManager
            recyclerview.addItemDecoration(GridSpaceDecoration())
            recyclerview.setHasFixedSize(true)
            recyclerview.itemAnimator = null
        }

        viewModel.count(title).observe(viewLifecycleOwner, Observer {
            binding?.appbar?.toolbar?.subtitle = getString(R.string.total, it)
        })

        viewModel.favoriteResult.observe(viewLifecycleOwner, Observer {
            it.isSuccess()?.apply {
                adapter.currentList[pos].favorite = tv.favorite
                adapter.notifyItemChanged(pos)
            }
        })

        viewModel.logoSearchResult.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(success = {
                it.data.tvs.forEach { newTv ->
                    adapter.currentList.find { oldTv ->
                        newTv.tvId == oldTv.tvId
                    }?.apply {
                        this.logo = newTv.logo
                        adapter.notifyItemChanged(adapter.currentList.indexOf(this))
                        logd("${this.name} ${this.logo}",TAG)
                    }
                }
            })
        })

        viewModel.tvs.observe(viewLifecycleOwner, Observer { result ->
            result.emit(
                {
                    adapter.submitList(it.data)
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
