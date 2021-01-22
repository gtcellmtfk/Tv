package com.bytebyte6.view.me

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bytebyte6.base.mvi.ResultObserver
import com.bytebyte6.base_ui.*
import com.bytebyte6.library.GridSpaceDecoration
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentPlayListBinding
import com.bytebyte6.view.download.DownloadServicePro
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.getViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

/***
 * 播放列表
 */
class PlaylistFragment : BaseShareFragment(R.layout.fragment_play_list), DownloadManager.Listener {

    companion object {
        fun newInstance(
            playlistId: Long,
            title: String,
            transitionName: String
        ): Fragment {
            return PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_PLAY_LIST_ID, playlistId)
                    putString(KEY_TITLE, title)
                    putString(KEY_TRANS_NAME, transitionName)
                }
            }
        }

        const val TAG = "PlaylistFragment"
    }

    private val httpDataSourceFactory by inject<HttpDataSource.Factory>()

    private val defaultRenderersFactory by lazy {
        DefaultRenderersFactory(requireContext())
    }

    private var downloadHelper: DownloadHelper? = null

    private val viewModel: PlaylistViewModel by viewModel()

    override fun onViewCreated(view: View): FragmentPlayListBinding {
        return FragmentPlayListBinding.bind(view).apply {

            setupToolbarArrowBack()

            toolbar.title = requireArguments().getString(KEY_TITLE)

            val adapter = ImageAdapter(ButtonType.DOWNLOAD) { pos ->
                if (!dialog.isShowing) {
                    onDownloadClick(pos, view)
                }
            }
            adapter.setOnItemClick { pos, _ ->
                showVideoActivity(adapter.currentList[pos].videoUrl)
            }
            adapter.setDoOnBind { pos, _ ->
                viewModel.searchLogo(pos)
            }
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null

            load(adapter, toolbar)
        }
    }

    private fun onDownloadClick(pos: Int, view: View) {
        viewModel.apply {
            showProgress()
            downloadHelper?.release()
            downloadHelper = DownloadHelper.forMediaItem(
                requireContext(),
                MediaItem.fromUri(getTv(pos).url),
                defaultRenderersFactory,
                httpDataSourceFactory
            )
            downloadHelper!!.prepare(object : DownloadHelper.Callback {
                override fun onPrepared(helper: DownloadHelper) {
                    download(pos)
                    DownloadServicePro.addDownload(requireContext(), getTv(pos).url)
                    val tip = getString(R.string.tip_add_download_has_been)
                    showSnack(view, Message(message = tip))
                    hideProgress()
                }

                override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                    if (e is DownloadHelper.LiveContentUnsupportedException) {
                        showSnack(
                            view,
                            Message(id = R.string.tip_un_support_download_live_stream)
                        )
                    } else {
                        showSnack(
                            view,
                            Message(message = e.message.toString())
                        )
                    }
                    hideProgress()
                }
            })
        }
    }

    private fun load(adapter: ImageAdapter, toolbar: MaterialToolbar) {
        viewModel.apply {
            tvs(requireArguments().getLong(KEY_PLAY_LIST_ID))
                .observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                    toolbar.subtitle = getString(R.string.total, adapter.itemCount)
                })
            updateTv.observe(viewLifecycleOwner, object : ResultObserver<UpdateTvParam>() {
                override fun successOnce(data: UpdateTvParam, end: Boolean) {
                    adapter.notifyItemChanged(data.pos)
                }

                override fun error(error: Throwable) {
                    showSnack(requireView(), Message(message = error.message.toString()))
                }
            })
        }
    }

    private val dialog by lazy {
        ProgressDialog(requireContext()).apply {
            setMessage(getString(R.string.tip_please_wait))
        }
    }

    private fun showProgress() {
        dialog.show()
    }

    private fun hideProgress() {
        dialog.dismiss()
    }

    override fun onDestroyView() {
        downloadHelper?.release()
        downloadHelper = null
        super.onDestroyView()
    }
}
