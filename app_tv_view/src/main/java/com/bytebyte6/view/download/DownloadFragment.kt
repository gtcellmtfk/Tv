package com.bytebyte6.view.download

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bytebyte6.base.logd
import com.bytebyte6.base.mvi.emit
import com.bytebyte6.base.mvi.runIfNotHandled
import com.bytebyte6.library.LinearSpaceDecoration
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.library.ListFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.view.showVideoActivity
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.Exception

/***
 * 下载中心
 */
class DownloadFragment : ListFragment(),DownloadManager.Listener{

    companion object {
        const val TAG = "DownloadFragment"
        fun newInstance() = DownloadFragment()
    }

    private val viewModel by viewModel<DownloadViewModel>()

    private val downloadManager by inject<DownloadManager>()

    private lateinit var adapter: DownloadAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcherBackToHome()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disEnabledSwipeRefreshLayout()

        downloadManager.addListener(this)

        toolbar.inflateMenu(R.menu.menu_download)
        toolbar.setOnMenuItemClickListener {
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
        setupToolbarMenuMode(getString(R.string.nav_download), "")

        adapter = DownloadAdapter()
        recyclerView.layoutManager = LinearLayoutManager(view.context)
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(LinearSpaceDecoration())
        recyclerView.itemAnimator=null

        adapter.setOnItemLongClick { pos, _ ->
            showDialog(pos)
            true
        }
        adapter.setOnItemClick { pos, _ ->
            showVideoActivity("", adapter.currentList[pos].download.request)
        }
        adapter.setOnCurrentListChanged { _, currentList ->
            emptyBox.isVisible = currentList.isEmpty()
        }

        viewModel.downloadList.observe(viewLifecycleOwner, Observer { result ->
            result.emit({
                adapter.submitList(it.data)
                hideSwipeRefresh()
            }, {
                it.runIfNotHandled {
                    hideSwipeRefresh()
                    showSnack(view, Message(message = it.error.message.toString()))
                }
            }, {
                it.runIfNotHandled {
                    showSwipeRefresh()
                }
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
            .setTitle(getString(R.string.tip_del_video).plus(adapter.currentList[pos].tv.name))
            .setMessage(getString(R.string.tip_beyond_retrieve))
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, i: Int ->
                DownloadServicePro.removeDownload(
                    requireContext(), adapter.currentList[pos].download.request.id)
                viewModel.deleteDownload(pos)
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
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

    override fun onViewCreated(view: View): ViewBinding? {
        return null
    }
}