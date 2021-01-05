package com.bytebyte6.view.video

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.view.KEY_TRANS_NAME
import com.bytebyte6.view.R
import com.bytebyte6.view.TvViewModel
import com.bytebyte6.view.databinding.FragmentVideoListBinding
import com.bytebyte6.view.setupToolbar
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoListFragment : BaseFragment<FragmentVideoListBinding>(R.layout.fragment_video_list) {

    companion object {
        const val TAG = "VideoListFragment"
        fun newInstance(transName: String): VideoListFragment {
            return VideoListFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_TRANS_NAME, transName)
                }
            }
        }
    }

    private val viewModel: TvViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? {
        return viewModel
    }

    override fun initBinding(view: View): FragmentVideoListBinding {
        return FragmentVideoListBinding.bind(view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.transitionName = requireArguments().getString(KEY_TRANS_NAME)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        var title = viewModel.getTitle()
        if (title.isEmpty()) {
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
            viewModel.ipTvsLiveData().observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                toolbar.subtitle = getString(R.string.total, it.size)
            })
        }
    }
}
