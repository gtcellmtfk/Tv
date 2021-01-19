package com.bytebyte6.view.player

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.bytebyte6.base.logd
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.KEY_CACHE
import com.bytebyte6.view.KEY_VIDEO_URL
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.util.MimeTypes
import org.koin.android.viewmodel.ext.android.viewModel


class PlayerFragment :
    BaseShareFragment/*<FragmentVideoBinding>*/(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "PlayerFragment"
        fun newInstance(bundle: Bundle) = PlayerFragment().apply {
            arguments = bundle
        }
    }

    private val viewModel by viewModel<PlayerViewModel>()

    private var player: Player? = null

    override fun initBinding(view: View): FragmentVideoBinding = FragmentVideoBinding.bind(view)

    override fun onResume() {
        super.onResume()
        playFromUrl()
    }

    override fun onStop() {
        super.onStop()
        stop()
    }

    private val listener: Player.EventListener = object : Player.EventListener {

        private var playbackStateReady = false

        override fun onPlaybackStateChanged(state: Int) {
            playbackStateReady = state == Player.STATE_READY
        }

        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            logd("onIsLoadingChanged $isLoading")
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            requireActivity().runOnUiThread {
                binding<FragmentVideoBinding>()?.progressBar?.isVisible =
                    !isPlaying && !playbackStateReady
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

    private fun playFromUrl() {
        binding<FragmentVideoBinding>()?.apply {
            val url = requireArguments().getString(KEY_VIDEO_URL)!!
            val request = requireArguments().getParcelable(KEY_CACHE) as DownloadRequest?
            player = SimpleExoPlayer.Builder(requireContext()).build()
            logd("url=$url")
            playerView.player = player
            val item =
                if (url.isNotEmpty())
                    MediaItem.Builder()
                        .setUri(url)
                        .setMimeType(MimeTypes.APPLICATION_M3U8)
                        .build()
                else
                    request!!.toMediaItem()
            player?.setMediaItem(item)
            player?.playWhenReady = true
            player?.prepare()
            player?.addListener(listener)
            playerView.keepScreenOn = true
            playerView.onResume()
        }
    }

    private fun stop() {
        binding<FragmentVideoBinding>()?.apply {
            playerView.keepScreenOn = false
            playerView.onPause()
            player?.removeListener(listener)
            player?.release()
            playerView.player = null
            player = null
        }
    }
}