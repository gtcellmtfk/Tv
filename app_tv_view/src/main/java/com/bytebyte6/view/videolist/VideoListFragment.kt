package com.bytebyte6.view.videolist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base.emit
import com.bytebyte6.base.runIfNotHandled
import com.bytebyte6.base.KEY_TRANS_NAME
import com.bytebyte6.base.Message
import com.bytebyte6.base.showSnack
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.library.GridSpaceDecoration
import com.bytebyte6.library.ListFragment
import com.bytebyte6.view.*
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

    private lateinit var adapter: ImageAdapter

    private val buttonClickListener = object : ButtonClickListener {
        override fun onClick(position: Int) {
            viewModel.fav(position)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()

        val title = requireArguments().getString(KEY_TITLE)!!
        setupToolbarArrowBack(title)
        viewModel.setKey(title)

        adapter = ImageAdapter(ButtonType.FAVORITE, buttonClickListener)
        adapter.onItemClick = { pos, _: View ->
            showVideoActivity(adapter.currentList[pos].videoUrl)
        }
        adapter.doOnBind = { pos, _: View ->
            viewModel.searchLogo(pos)
        }
        adapter.onCurrentListChanged = { _, currentList ->
            binding?.emptyBox?.isVisible = currentList.isEmpty()
        }
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

    override fun onDestroyView() {
        adapter.reset()
        super.onDestroyView()
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

    override fun onRefresh() {
        //not to do
    }
}
