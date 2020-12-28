package com.bytebyte6.view

import android.os.Bundle
import android.view.View
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoFragment(private val iptv: IpTv) :
    BaseFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "VideoFragment"
    }

    private val viewModel by sharedViewModel<IpTvViewModel>()

    private val simpleExoPlayer by inject<Player>()

    override fun initViewModel(): BaseViewModel = viewModel

    override fun initBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            playerView.player = simpleExoPlayer
            simpleExoPlayer.setMediaItem(MediaItem.fromUri(iptv.url))
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.prepare()
        }
    }

    override fun onDestroyView() {
        simpleExoPlayer.release()
        binding?.playerView?.player = null
        super.onDestroyView()
    }
}