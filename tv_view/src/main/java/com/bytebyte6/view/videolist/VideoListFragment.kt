package com.bytebyte6.view.videolist

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentVideoListBinding
import com.bytebyte6.view.video.VideoActivity
import com.bytebyte6.view.video.VideoAdapter
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoListFragment : BaseFragment<FragmentVideoListBinding>(R.layout.fragment_video_list) {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun initBinding(view: View): FragmentVideoListBinding {
        return FragmentVideoListBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.transitionName = requireArguments().getString(KEY_TRANS_NAME)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        var title = requireArguments().getString(KEY_ITEM)
        if (title.isNullOrEmpty()) {
            title = getString(R.string.home_other)
        }

        val adapter = VideoAdapter()

        adapter.setOnItemClick { pos, _ ->
            startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(Tv.TAG, adapter.currentList[pos])
            })
        }

        binding?.apply {
            toolbar.title = title
            recyclerView.adapter = adapter
            setupToolbar()
            viewModel.search(title).observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                toolbar.subtitle = getString(R.string.total, it.size)
            })
        }
    }
}
