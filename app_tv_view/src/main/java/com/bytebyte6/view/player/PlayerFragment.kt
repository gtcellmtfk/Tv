package com.bytebyte6.view.player

import android.content.Context
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.view.View
import androidx.core.view.isVisible
import com.bytebyte6.base.logd
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.KEY_VIDEO_URL
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.MimeTypes
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class PlayerFragment :
    BaseFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "PlayerFragment"
    }

    private val viewModel by viewModel<PlayerViewModel>()

    private val simpleExoPlayer by inject<Player>()

    override fun initBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().requestedOrientation=SCREEN_ORIENTATION_LANDSCAPE
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().requestedOrientation=SCREEN_ORIENTATION_PORTRAIT
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
            val url=requireArguments().getString(KEY_VIDEO_URL)
            logd("url=$url")
            playerView.player = simpleExoPlayer
            val item = MediaItem.Builder()
                .setUri(url)
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