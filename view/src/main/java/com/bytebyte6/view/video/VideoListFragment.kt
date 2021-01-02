package com.bytebyte6.view.video

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.view.IpTvViewModel
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoListBinding
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoListFragment : BaseFragment<FragmentVideoListBinding>(R.layout.fragment_video_list) {

    companion object {
        const val TAG = "VideoListFragment"
    }

    private val viewModel: IpTvViewModel by sharedViewModel()

    private val args by navArgs<VideoListFragmentArgs>()

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
        view.transitionName = args.transName

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        var title = viewModel.getTitle()
        if (title.isEmpty()) {
            title = getString(R.string.home_other)
        }

        val adapter = VideoAdapter()

        adapter.setOnItemClick { pos, _ ->
            startActivity(Intent(context, VideoActivity::class.java).apply {
                putExtra(IpTv.TAG, adapter.currentList[pos])
            })
        }

        val navController = findNavController(this@VideoListFragment)

        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding?.apply {
            toolbar.title = title
            recyclerView.adapter = adapter
            toolbar.setupWithNavController(navController, appBarConfiguration)
            viewModel.ipTvsLiveData()?.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                toolbar.subtitle = getString(R.string.total, it.size)
            })
        }
    }
}
