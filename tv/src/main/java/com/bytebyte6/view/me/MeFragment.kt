package com.bytebyte6.view.me

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
import com.bytebyte6.common.*
import com.bytebyte6.utils.LinearSpaceDecoration
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.PlaylistAdapter
import com.bytebyte6.view.databinding.FragmentMeBinding
import com.bytebyte6.view.meToPlaylist
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.viewmodel.MeViewModel
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
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                viewModel.delete(selectionTracker.selection)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int ->
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
                playlistAdapter.list[pos].playlistId,
                playlistAdapter.list[pos].playlistName,
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
        viewModel.deleteResult.observe(viewLifecycleOwner, Observer {
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
        viewModel.playlists.observe(viewLifecycleOwner, Observer {
            playlistAdapter.replace(it)
            binding?.apply {
                lavEmpty.isVisible = it.isEmpty()
            }
        })
        viewModel.parseResult.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(
                    {
                        hideProgressBar()
                    }, {
                hideProgressBar()
                if (it.error is UnsupportedOperationException) {
                    showSnack(view, R.string.tip_not_m3u_m3u8_file)
                } else {
                    showSnack(
                            view,
                            getString(R.string.tip_parse_file_error)
                    )
                }
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