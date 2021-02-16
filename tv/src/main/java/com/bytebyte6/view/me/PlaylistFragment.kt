package com.bytebyte6.view.me

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.common.*
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.TvAdapter
import com.bytebyte6.view.download.DownloadServicePro
import com.bytebyte6.viewmodel.PlaylistViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.upstream.HttpDataSource
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

import java.io.IOException

/***
 * 播放列表
 */
class PlaylistFragment : ListFragment(), ButtonClickListener {

    companion object {
        fun newInstance(playlistId: Long, title: String, transitionName: String): Fragment {
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

    private val defaultRenderersFactory by lazy { DefaultRenderersFactory(requireContext()) }

    private val viewModel: PlaylistViewModel by viewModel()

    private val networkHelper by inject<NetworkHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnSharedElementReturnTransitionEnd {
            clearRecyclerView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarArrowBack {
            binding?.emptyBox?.cancelAnimation()
        }
        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()

        val tvAdapter = TvAdapter(this, download = true)

        tvAdapter.onItemClick = { pos, _: View ->
            toPlayer(tvAdapter.currentList[pos].url)
        }

        imageClearHelper = tvAdapter

        binding?.apply {
            appbar.toolbar.title = requireArguments().getString(KEY_TITLE)
            recyclerView.adapter = tvAdapter
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
        }

        viewModel.playlistId = requireArguments().getLong(KEY_PLAY_LIST_ID)
        viewModel.tvs.observe(viewLifecycleOwner, Observer { result ->
            result.emit({
                tvAdapter.submitList(it.data.toList())
                end = it.end
                hideSwipeRefresh()
                hideProgress()
            }, {
                view.longSnack(it.error.message.toString())
                hideSwipeRefresh()
                hideProgress()
            }, {
                showProgress()
            })
        })
        viewModel.count.observe(viewLifecycleOwner, Observer {
            binding?.appbar?.toolbar?.subtitle = getString(R.string.total, it)
            binding?.emptyBox?.isVisible = it == 0
        })

        viewModel.itemChanged.observe(viewLifecycleOwner, Observer { pos ->
            tvAdapter.notifyItemChanged(pos)
        })

        viewModel.first()
    }

    private fun showTipDialog(tv: Tv) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.tip)
            .setMessage(getString(R.string.tip_confirm_the_download))
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.enter) { dialog, _ ->
                dialog.dismiss()
                onDownloadClick(tv)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun onDownloadClick(tv: Tv) {
        val dialog = showProgressDialog()
        val downloadHelper = DownloadHelper.forMediaItem(
            requireContext(),
            MediaItem.fromUri(tv.url),
            defaultRenderersFactory,
            httpDataSourceFactory
        )
        downloadHelper.prepare(getDownloadHelperCallback(tv, dialog))
    }

    private fun getDownloadHelperCallback(
        tv: Tv,
        dialog: ProgressDialog2
    ): DownloadHelper.Callback {
        return object : DownloadHelper.Callback {
            override fun onPrepared(helper: DownloadHelper) {
                DownloadServicePro.addDownload(requireContext(), tv.url)
                val tip = getString(R.string.tip_add_download_has_been)
                requireView().longSnack(tip)
                dialog.dismiss()
            }

            override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                if (e is DownloadHelper.LiveContentUnsupportedException) {
                    requireView().longSnack(R.string.tip_un_support_download_live_stream)
                } else {
                    requireView().longSnack(e.message.toString())
                }
                dialog.dismiss()
            }
        }
    }

    private fun showProgressDialog(): ProgressDialog2 {
        val progressDialog = ProgressDialog2(requireContext()).apply {
            setTitle(R.string.tip)
            setMessage2(getString(R.string.tip_please_wait))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
        }
        progressDialog.show()
        return progressDialog
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

    override fun onRefresh() = Unit

    override fun onClick(position: Int, tv: Tv) {
        when (networkHelper.getNetworkType()) {
            NetworkHelper.NetworkType.MOBILE -> {
                showTipDialog(tv)
            }
            NetworkHelper.NetworkType.WIFI -> {
                onDownloadClick(tv)
            }
            else -> {
                requireView().longSnack(R.string.network_disconnected)
            }
        }
    }
}
