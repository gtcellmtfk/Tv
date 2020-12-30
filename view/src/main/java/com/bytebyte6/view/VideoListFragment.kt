package com.bytebyte6.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.transition.TransitionInflater
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.data.model.Languages
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.view.databinding.FragmentVideoListBinding
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoListFragment : BaseFragment<FragmentVideoListBinding>(R.layout.fragment_video_list) {

    companion object {
        const val TAG = "VideoListFragment"
    }

    private val viewModel: IpTvViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        sharedElementEnterTransition = TransitionInflater.from(requireContext())
//            .inflateTransition(R.transition.share_enter)
//        sharedElementReturnTransition = TransitionInflater.from(requireContext())
//            .inflateTransition(R.transition.share_exit)
        sharedElementEnterTransition = MaterialContainerTransform()
        exitTransition=Hold().apply { addTarget(R.id.home_root) }
    }

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? {
        return viewModel
    }

    override fun initBinding(view: View): FragmentVideoListBinding {

        postponeEnterTransition()

//        view.transitionName="share"

        return FragmentVideoListBinding.bind(view).apply {
            when (val item = viewModel.clickItem) {
                is Country -> {
                    toolbar.title = item.countryName
                }
                is Category -> {
                    toolbar.title = item.category
                }
                is Languages -> {
                    toolbar.title = item.getString()
                }
            }
            val adapter = VideoAdapter()
            adapter.setOnItemClick { pos, _ ->
                startActivity(Intent(context, VideoActivity::class.java).apply {
                    putExtra(IpTv.TAG, adapter.currentList[pos])
                })
            }
            recyclerView.adapter = adapter

            val navController = findNavController(this@VideoListFragment)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)

            viewModel.ipTvsLiveData()?.observe(this@VideoListFragment, Observer {
                adapter.submitList(it)
                toolbar.subtitle = getString(R.string.total, it.size)
                startPostponedEnterTransition()
            })
        }
    }

}
