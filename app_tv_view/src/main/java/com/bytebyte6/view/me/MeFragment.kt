package com.bytebyte6.view.me

import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionTracker
import com.bytebyte6.base.mvi.emitIfNotHandled
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.library.LinearSpaceDecoration
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentMeBinding
import org.koin.android.viewmodel.ext.android.viewModel

/***
 * 导入
 */
class MeFragment : BaseShareFragment<FragmentMeBinding>(R.layout.fragment_me) {

    companion object {
        const val TAG = "MeFragment"
    }

    private val viewModel: MeViewModel by viewModel()

    private val getContent = ActivityResultContracts.GetContent()

    private val launcher by lazy {
        val callback = ActivityResultCallback<Uri?> { result ->
            if (result != null) {
                viewModel.parseM3u(result)
            }
        }
        registerForActivityResult(getContent, callback)
    }

    private lateinit var playlistAdapter: PlaylistAdapter
    private lateinit var selectionTracker: SelectionTracker<Long>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcherBackToHome()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbarMenuMode()
        playlistAdapter = PlaylistAdapter()
        playlistAdapter.onItemClick = { pos, view1 ->
            replaceWithShareElement(
                PlaylistFragment.newInstance(
                    viewModel.getPlaylistId(pos),
                    playlistAdapter.list[pos].title,
                    playlistAdapter.list[pos].transitionName
                ),
                PlaylistFragment.TAG,
                view1
            )
        }
        binding?.apply {
            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/m3u")
                    true
                }
            }
            recyclerView.adapter = playlistAdapter
            playlistAdapter.setupSelectionTracker(recyclerView,
                object : SelectionTracker.SelectionObserver<Long>() {
                    override fun onSelectionChanged() {
                        val hasSelection = selectionTracker.hasSelection()
                        if (hasSelection) {
                            fab.show()
                        } else {
                            fab.hide()
                        }
                    }
                })
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
                selectionTracker.clearSelection()
                showSnack(view, Message(id = R.string.tip_del_success))
                binding?.apply {
                    fab.hide()
                }
            }, {
                showSnack(view, Message(id = R.string.tip_del_fail))
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
                    replace(
                        PlaylistFragment.newInstance(
                            it.data.playlistId,
                            it.data.playlistName,
                            it.data.transitionName
                        ), PlaylistFragment.TAG
                    )
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