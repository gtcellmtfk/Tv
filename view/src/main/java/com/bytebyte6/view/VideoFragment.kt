package com.bytebyte6.view

import android.os.Bundle
import android.view.View
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.model.IpTv
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel

class VideoFragment :
    BaseFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "VideoFragment"
    }

    private val viewModel by sharedViewModel<IpTvViewModel>()

    private val simpleExoPlayer by inject<Player>()

    private lateinit var ipTv: IpTv

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? = viewModel

    override fun initBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ipTv = requireArguments().get(IpTv.TAG) as IpTv
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private fun play() {
        binding?.apply {
            playerView.player = simpleExoPlayer
            simpleExoPlayer.setMediaItem(MediaItem.fromUri(ipTv.url))
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.prepare()
            playerView.onResume()
        }
    }

    private fun stop() {
        binding?.apply {
            playerView.onPause()
            simpleExoPlayer.release()
            playerView.player = null
        }
    }
}