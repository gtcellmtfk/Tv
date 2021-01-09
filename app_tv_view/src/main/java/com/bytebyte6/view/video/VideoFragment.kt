package com.bytebyte6.view.video

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class VideoFragment :
    BaseFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "VideoFragment"
    }

    private val viewModel by viewModel<VideoViewModel>()

    private val simpleExoPlayer by inject<Player>()

    private lateinit var tv: Tv

    override fun initBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv = requireArguments().get(Tv.TAG) as Tv
    }

    override fun onResume() {
        super.onResume()
        play()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private val listener: Player.EventListener = object : Player.EventListener {

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            requireActivity().runOnUiThread {
                binding?.progressBar?.isVisible=!isPlaying
            }
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            val m = if (error.message != null) {
                Message(
                    actionId = R.string.tip_play_retry,
                    message = error.message!!,
                    longDuration = true
                )
            } else {
                Message(
                    actionId = R.string.tip_play_retry,
                    message = getString(R.string.tip_play_error),
                    longDuration = true
                )
            }
            requireActivity()
                .runOnUiThread {
                    showSnack(requireView(), m)
                }
        }
    }

    private fun play() {
        binding?.apply {
            playerView.player = simpleExoPlayer
            val item = MediaItem.Builder()
                .setUri(tv.url)
                .setMimeType(MimeTypes.APPLICATION_M3U8)
                .build()
            simpleExoPlayer.setMediaItem(item)
            simpleExoPlayer.playWhenReady = true
            simpleExoPlayer.prepare()
            simpleExoPlayer.addListener(listener)
            playerView.keepScreenOn = true
            playerView.onResume()
        }
    }

    private fun stop() {
        binding?.apply {
            playerView.keepScreenOn = false
            playerView.onPause()
            simpleExoPlayer.removeListener(listener)
            simpleExoPlayer.release()
            playerView.player = null
        }
    }
}