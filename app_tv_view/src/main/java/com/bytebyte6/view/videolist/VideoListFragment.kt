package com.bytebyte6.view.videolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base_ui.KEY_TRANS_NAME
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.base_ui.ListFragment
import com.bytebyte6.base_ui.databinding.FragmentListBinding
import com.bytebyte6.base_ui.setupToolbarPopUp
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.TvFts
import com.bytebyte6.view.KEY_ITEM
import com.bytebyte6.view.R
import com.bytebyte6.view.video.VideoActivity
import com.bytebyte6.view.video.VideoAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoListFragment : ListFragment() {

    companion object {
        const val TAG = "VideoListFragment"
        fun newInstance(transName: String, item: String): VideoListFragment {
            return VideoListFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TRANS_NAME, transName)
                    putString(KEY_ITEM, item)
                }
            }
        }
    }

    private val viewModel: VideoListViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disEnabledSwipeRefreshLayout()

        showSwipeRefresh()

        val title = requireArguments().getString(KEY_ITEM)!!

        val adapter = VideoAdapter()

        adapter.setOnItemClick { pos, _ ->
            startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(Tv.TAG, adapter.currentList[pos])
            })
        }

        toolbar.title = title
        recyclerView.adapter = adapter
        setupToolbarPopUp()

        recyclerView.layoutManager = GridLayoutManager(view.context, 2)

        viewModel.search(title).observe(viewLifecycleOwner, Observer {
            toolbar.subtitle = getString(R.string.total, it)
        })

        viewModel.tvs(title).observe(viewLifecycleOwner, EventObserver {
            when (it) {
                is Result.Success -> {
                    adapter.submitList(TvFts.toIpTvs(it.data))
                    end = it.end
                    hideSwipeRefresh()
                    hideProgress()
                    emptyBox.isVisible = adapter.currentList.isEmpty()
                }
                is Result.Error -> {
                    showSnack(view,
                        Message(message = it.error.message.toString())
                    )
                    hideSwipeRefresh()
                    hideProgress()
                }
                Result.Loading -> {
                    showProgress()
                }
            }
        })

        onLoadMore()
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
