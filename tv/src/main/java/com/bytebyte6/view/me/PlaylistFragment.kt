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
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.TvAdapter
import com.bytebyte6.view.download.DownloadServicePro
import com.bytebyte6.viewmodel.PlaylistViewModel
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.offline.DownloadHelper
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.upstream.HttpDataSource
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.io.IOException

/***
 * 播放列表
 */
class PlaylistFragment : ListFragment(), DownloadManager.Listener, ButtonClickListener {

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

    private var downloadHelper: DownloadHelper? = null

    private val viewModel: PlaylistViewModel by viewModel()

    private var progressDialog: ProgressDialog2? = null

    private val networkHelper by inject<NetworkHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doOnSharedElementReturnTransitionEnd {
            clearRecyclerView()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarArrowBack()
        disEnabledSwipeRefreshLayout()
        showSwipeRefresh()

        val tvAdapter = TvAdapter(ButtonType.DOWNLOAD, this)

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
                showSnack(view, it.error.message.toString())
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
        viewModel.downloadResult.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(success = {
                tvAdapter.notifyItemChanged(it.data.pos)
            }, error = {
                showSnack(requireView(), it.error.message.toString())
            })
        })
        viewModel.first()
    }

    private fun showTipDialog(position: Int, tv: Tv) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.tip)
            .setMessage(getString(R.string.tip_confirm_the_download))
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.enter) { dialog, _ ->
                dialog.dismiss()
                onDownloadClick(position, tv)
            }
            .setCancelable(false)
            .create()
            .show()
    }

    private fun onDownloadClick(pos: Int, tv: Tv) {
        showProgressDialog()
        downloadHelper?.release()
        downloadHelper = null
        downloadHelper = DownloadHelper.forMediaItem(
            requireContext(),
            MediaItem.fromUri(tv.url),
            defaultRenderersFactory,
            httpDataSourceFactory
        )
        downloadHelper!!.prepare(getDownloadHelperCallback(pos, tv))
    }

    private fun getDownloadHelperCallback(pos: Int, tv: Tv): DownloadHelper.Callback {
        return object : DownloadHelper.Callback {
            override fun onPrepared(helper: DownloadHelper) {
                viewModel.download(pos, tv)
                DownloadServicePro.addDownload(requireContext(), tv.url)
                val tip = getString(R.string.tip_add_download_has_been)
                showSnack(requireView(), tip)
                hideProgressDialog()
            }

            override fun onPrepareError(helper: DownloadHelper, e: IOException) {
                if (e is DownloadHelper.LiveContentUnsupportedException) {
                    showSnack(requireView(), R.string.tip_un_support_download_live_stream)
                } else {
                    showSnack(requireView(), Message(message = e.message.toString()))
                }
                hideProgressDialog()
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog2(requireContext()).apply {
            setTitle(R.string.tip)
            setMessage2(getString(R.string.tip_please_wait))
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
                this@PlaylistFragment.progressDialog = null
                downloadHelper?.release()
                downloadHelper = null
            }
        }
        progressDialog!!.show()
    }

    private fun hideProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onDestroyView() {
        downloadHelper?.release()
        downloadHelper = null
        super.onDestroyView()
    }

    override fun onLoadMore() {
        viewModel.loadMore()
    }

    override fun onRefresh() = Unit

    override fun onClick(position: Int, tv: Tv) {
        when (networkHelper.getNetworkType()) {
            NetworkHelper.NetworkType.MOBILE -> {
                showTipDialog(position, tv)
            }
            NetworkHelper.NetworkType.WIFI -> {
                onDownloadClick(position, tv)
            }
            else -> {
                showSnack(requireView(), R.string.network_disconnected)
            }
        }
    }
}
