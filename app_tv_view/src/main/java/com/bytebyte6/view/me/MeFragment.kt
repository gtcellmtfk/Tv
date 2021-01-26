package com.bytebyte6.view.me

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionTracker
import com.bytebyte6.base.*
import com.bytebyte6.library.LinearSpaceDecoration
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentMeBinding
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 导入
 * 1、本地导入ok
 * 2、可从网络链接导入todo
 */
class MeFragment : BaseShareFragment<FragmentMeBinding>(R.layout.fragment_me) {

    companion object {
        const val TAG = "MeFragment"
    }

    private val viewModel: MeViewModel by viewModel()

    private lateinit var launcher: ActivityResultLauncher<String>

    private lateinit var selectionTracker: SelectionTracker<Long>

    private val selectionObserver = object : SelectionTracker.SelectionObserver<Long>() {
        override fun onSelectionChanged() {
            val hasSelection = selectionTracker.hasSelection()
            if (hasSelection) {
                binding?.fab?.show()
            } else {
                binding?.fab?.hide()
            }
        }
    }

    override fun initViewBinding(view: View): FragmentMeBinding {
        return FragmentMeBinding.bind(view)
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tip_del_playlist))
            .setMessage(getString(R.string.tip_beyond_retrieve))
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                viewModel.delete(selectionTracker.selection)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = ActivityResultCallback<Uri?> { result ->
            if (result != null) {
                viewModel.parseM3u(result)
            }
        }
        val getContent = ActivityResultContracts.GetContent()
        launcher = registerForActivityResult(getContent, callback)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode()
        doOnExitTransitionEndOneShot {
            clearRecyclerView()
        }
        setupOnBackPressedDispatcherBackToHome()
        val playlistAdapter = PlaylistAdapter()
        playlistAdapter.onItemClick = { pos, itemView ->
            meToPlaylist(
                viewModel.getPlaylistId(pos),
                playlistAdapter.list[pos].title,
                playlistAdapter.list[pos].transitionName,
                itemView
            )
        }

        binding?.apply {
            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/*")
                    true
                }
            }
            this@MeFragment.recyclerView = recyclerView
            recyclerView.adapter = playlistAdapter
            playlistAdapter.setupSelectionTracker(recyclerView, selectionObserver)
            selectionTracker = playlistAdapter.selectionTracker!!
            recyclerView.addItemDecoration(LinearSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            fab.setOnClickListener {
                selectionTracker.selection.apply {
                    if (!this.isEmpty) {
                        showDialog()
                    }
                }
            }
        }
        viewModel.deletePlaylist.observe(viewLifecycleOwner, Observer {
            it.emitIfNotHandled({
                hideProgressBar()
                selectionTracker.clearSelection()
                showSnack(view, Message(id = R.string.tip_del_success))
                binding?.apply {
                    fab.hide()
                }
            }, {
                hideProgressBar()
                showSnack(view, Message(id = R.string.tip_del_fail))
            }, {
                showProgressBar()
            })
        })
        viewModel.playlistNames.observe(viewLifecycleOwner, Observer {
            playlistAdapter.replace(it)
            binding?.apply {
                lavEmpty.isVisible = it.isEmpty()
            }
        })
        viewModel.playlist.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(
                {
                    hideProgressBar()
                    meToPlaylist(
                            it.data.playlistId,
                            it.data.playlistName,
                            it.data.transitionName)
                }, {
                    hideProgressBar()
                    showSnack(
                        view,
                        Message(id = (R.string.tip_parse_file_error))
                    )
                }, {
                    showProgressBar()
                }
            )
        })
    }

    private fun showProgressBar() {
        binding?.apply {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding?.apply {
            progressBar.visibility = View.GONE
        }
    }
}