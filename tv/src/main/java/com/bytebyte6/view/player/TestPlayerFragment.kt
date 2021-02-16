package com.bytebyte6.view.player

import android.os.Bundle
import android.view.View
import com.bytebyte6.common.BaseShareFragment
import com.bytebyte6.common.Message
import com.bytebyte6.common.logd
import com.bytebyte6.common.longSnack
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentVideoBinding
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer



class TestPlayerFragment :
    BaseShareFragment<FragmentVideoBinding>(R.layout.fragment_video) {

    companion object {
        const val TAG: String = "TestPlayerFragment"
        fun newInstance(bundle: Bundle) = TestPlayerFragment().apply {
            arguments = bundle
        }
    }

    private var player: Player? = null

    override fun onResume() {
        super.onResume()
        start()
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
            logd("onIsLoadingChanged$isLoading")
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            logd("onIsPlayingChanged$isPlaying")
        }

        override fun onPlayerError(error: ExoPlaybackException) {
            error.printStackTrace()
            val m = if (error.message != null) {
                Message(
                    actionStringId = R.string.tip_play_retry,
                    message = error.message!!,
                    longDuration = true
                )
            } else {
                Message(
                    actionStringId = R.string.tip_play_retry,
                    message = getString(R.string.tip_play_error),
                    longDuration = true
                )
            }
            requireActivity()
                .runOnUiThread {
                    requireView().longSnack(m.get(requireContext()))
                }
        }
    }

    private fun start() {
        binding?.apply {
            val url = "rtmp://ivi.bupt.edu.cn:1935/livetv/starsports"
//            val url = "https://streaming-live.rtp.pt/liverepeater/smil:rtp1.smil/playlist.m3u8"
//            val url = "mms://202.160.15.88/stream-01-128"
//            val url = "rtsp://stream.teracomm.bg/balkanika"
            player = SimpleExoPlayer.Builder(requireContext())
                .build()
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .build()
            player!!.setMediaItem(mediaItem)
            player!!.playWhenReady = true
            player!!.prepare()
            player!!.addListener(listener)
            playerView.player = player
            playerView.keepScreenOn = true
            playerView.onResume()
        }
    }

    private fun stop() {
        binding?.apply {
            playerView.keepScreenOn = false
            playerView.onPause()
            player?.removeListener(listener)
            player?.release()
            playerView.player = null
            player = null
        }
    }

    override fun initViewBinding(view: View): FragmentVideoBinding? {
        return FragmentVideoBinding.bind(view)
    }
}