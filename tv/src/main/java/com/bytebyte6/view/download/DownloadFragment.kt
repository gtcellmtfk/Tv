package com.bytebyte6.view.download

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytebyte6.common.*
import com.bytebyte6.utils.LinearSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.view.toPlayer
import com.bytebyte6.viewmodel.DownloadViewModel
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 下载中心
 */
class DownloadFragment : ListFragment(), DownloadManager.Listener, Toolbar.OnMenuItemClickListener {

    companion object {
        const val TAG = "DownloadFragment"
        fun newInstance() = DownloadFragment()
    }

    private val viewModel by viewModel<DownloadViewModel>()

    private val downloadManager by inject<DownloadManager>()

    private val networkHelper by inject<NetworkHelper>()

    private lateinit var downloadAdapter: DownloadAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedDispatcherBackToHome()

        disEnabledSwipeRefreshLayout()

        downloadManager.addListener(this)

        setupToolbarMenuMode(getString(R.string.nav_download), "")

        doOnExitTransitionEndOneShot {
            clearRecyclerView()
        }

        downloadAdapter = DownloadAdapter().apply {
            onItemLongClick = { pos, _: View ->
                showDeleteVideoDialog(pos)
                true
            }
            onItemClick = { pos, _: View ->
                toPlayer("", currentList[pos].download.request)
            }
            onCurrentListChanged = { _, currentList ->
                binding?.emptyBox?.isVisible = currentList.isEmpty()
            }
        }
        imageClearHelper = downloadAdapter

        binding?.apply {
            appbar.toolbar.inflateMenu(R.menu.menu_download)
            appbar.toolbar.setOnMenuItemClickListener(this@DownloadFragment)
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.adapter = downloadAdapter
            recyclerView.setHasFixedSize(true)
            recyclerView.addItemDecoration(LinearSpaceDecoration())
            recyclerView.itemAnimator = null
        }

        viewModel.downloadListResult.observe(viewLifecycleOwner, Observer { result ->
            result.emit({
                downloadAdapter.submitList(it.data)
                hideSwipeRefresh()
            }, {
                hideSwipeRefresh()
                showSnack(view, it.error.message.toString())
            }, {
                showSwipeRefresh()
            })
        })
    }

    private fun showDeleteVideoDialog(pos: Int) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tip_del_video).plus(downloadAdapter.currentList[pos].tv.name))
            .setMessage(getString(R.string.tip_beyond_retrieve))
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, _: Int ->
                DownloadServicePro.removeDownload(
                    requireContext(), downloadAdapter.currentList[pos].download.request.id
                )
                viewModel.deleteDownload(pos)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun startDownload() {
        DownloadServicePro.resumeDownloads(requireContext())
        showSnack(requireView(), R.string.resume)
        viewModel.startInterval()
    }

    override fun onDestroyView() {
        downloadManager.removeListener(this)
        super.onDestroyView()
    }

    override fun onDownloadChanged(
        downloadManager: DownloadManager,
        download: Download,
        finalException: Exception?
    ) {
        logd("onDownloadChanged request=${download.request}")
    }

    override fun onDownloadRemoved(downloadManager: DownloadManager, download: Download) {
        logd("onDownloadRemoved request=${download.request}")
    }

    override fun onDownloadsPausedChanged(
        downloadManager: DownloadManager,
        downloadsPaused: Boolean
    ) {
        logd("onDownloadsPausedChanged downloadsPaused=$downloadsPaused")
    }

    override fun onIdle(downloadManager: DownloadManager) {
        logd("onIdle")
        viewModel.pauseInterval()
    }

    override fun onLoadMore() = Unit

    override fun onRefresh() = Unit

    override fun onMenuItemClick(item: MenuItem): Boolean {
        if (viewModel.downloadListResult.getSuccessData().isNullOrEmpty()) {
            return false
        } else {
            when (item.itemId) {
                R.id.pause -> {
                    DownloadServicePro.pauseDownloads(requireContext())
                    showSnack(requireView(), R.string.pause)
                    viewModel.pauseInterval()
                }
                R.id.resume -> {
                    val networkType = networkHelper.getNetworkType()
                    if (networkType == NetworkHelper.NetworkType.WIFI) {
                        startDownload()
                    } else if (networkType == NetworkHelper.NetworkType.MOBILE) {
                        showTipDialog()
                    }
                }
            }
            return true
        }
    }

    private fun showTipDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.tip)
            .setMessage(getString(R.string.tip_confirm_the_download))
            .setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.enter) { dialog, _ ->
                dialog.dismiss()
                startDownload()
            }
            .setCancelable(false)
            .create()
            .show()
    }
}