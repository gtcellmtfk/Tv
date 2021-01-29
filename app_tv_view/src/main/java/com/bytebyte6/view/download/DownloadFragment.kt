package com.bytebyte6.view.download

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bytebyte6.base.*
import com.bytebyte6.library.LinearSpaceDecoration
import com.bytebyte6.library.ListFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.view.toPlayer
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 下载中心
 */
class DownloadFragment : ListFragment(), DownloadManager.Listener {

    companion object {
        const val TAG = "DownloadFragment"
        fun newInstance() = DownloadFragment()
    }

    private val viewModel by viewModel<DownloadViewModel>()

    private val downloadManager by inject<DownloadManager>()

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
                showDialog(pos)
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

        binding?.run {
            appbar.toolbar.inflateMenu(R.menu.menu_download)
            appbar.toolbar.setOnMenuItemClickListener {
                if (viewModel.downloadList.getSuccessData().isNullOrEmpty()) {
                    false
                } else {
                    when (it.itemId) {
                        R.id.pause -> {
                            DownloadServicePro.pauseDownloads(requireContext())
                            showSnack(requireView(), Message(id = R.string.pause))
                            viewModel.pauseInterval()
                        }
                        R.id.resume -> {
                            DownloadServicePro.resumeDownloads(requireContext())
                            showSnack(requireView(), Message(id = R.string.resume))
                            viewModel.startInterval()
                        }
                    }
                    true
                }
            }
            recyclerview.layoutManager = LinearLayoutManager(view.context)
            recyclerview.adapter = downloadAdapter
            recyclerview.setHasFixedSize(true)
            recyclerview.addItemDecoration(LinearSpaceDecoration())
            recyclerview.itemAnimator = null
        }

        viewModel.downloadList.observe(viewLifecycleOwner, Observer { result ->
            result.emit({
                downloadAdapter.submitList(it.data)
                hideSwipeRefresh()
            }, {
                hideSwipeRefresh()
                showSnack(view, Message(message = it.error.message.toString()))
            }, {
                showSwipeRefresh()
            }
            )
        })
    }

    override fun onDestroyView() {
        downloadManager.removeListener(this)
        super.onDestroyView()
    }

    private fun showDialog(pos: Int) {
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

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }
}