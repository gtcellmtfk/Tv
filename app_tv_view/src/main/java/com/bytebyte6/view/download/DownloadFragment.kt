package com.bytebyte6.view.download

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import com.bytebyte6.base.mvi.ResultObserver
import com.bytebyte6.base.mvi.emit
import com.bytebyte6.base.mvi.runIfNotHandled
import com.bytebyte6.base_ui.LinearSpaceDecoration
import com.bytebyte6.base_ui.ListFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.view.R
import com.bytebyte6.view.card.getSelectionTracker
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.view.showVideoActivity
import com.google.android.exoplayer2.offline.DownloadManager
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DownloadFragment : ListFragment() {

    companion object {
        const val TAG = "DownloadFragment"
        fun newInstance() = DownloadFragment()
    }

    private val viewModel by viewModel<DownloadViewModel>()

    private lateinit var adapter: DownloadAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcherBackToHome()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu_download)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.pause -> {
                    RtmpDownloadService.pauseDownloads(requireContext())
                    showSnack(requireView(), Message(id = R.string.pause))
                }
                R.id.resume -> {
                    RtmpDownloadService.resumeDownloads(requireContext())
                    showSnack(requireView(), Message(id = R.string.resume))
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

        adapter.setOnItemClick { pos, _ ->
            showVideoActivity("", viewModel.getRequest(pos))
        }
        adapter.setOnCurrentListChanged { _, currentList ->
            emptyBox.isVisible = currentList.isEmpty()
        }

        viewModel.list.observe(viewLifecycleOwner, Observer { result ->
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

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tip_del_video))
            .setMessage(getString(R.string.tip_beyond_retrieve))
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }

    override fun initBinding(view: View): ViewBinding? {
        return null
    }
}