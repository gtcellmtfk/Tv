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
import com.bytebyte6.base.mvi.emitIfNotHandled
import com.bytebyte6.base_ui.*
import com.bytebyte6.view.*
import com.bytebyte6.view.R
import com.bytebyte6.view.card.CardAdapter
import com.bytebyte6.view.databinding.FragmentMeBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MeFragment : BaseShareFragment/*<FragmentMeBinding>*/(R.layout.fragment_me) {

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

    private val cardAdapter = CardAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setupOnBackPressedDispatcherBackToHome()
    }

    override fun initBinding(view: View): FragmentMeBinding {
        return FragmentMeBinding.bind(view).apply {

            setupToolbarMenuMode()

            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/m3u")
                    true
                }
            }

            recyclerView.adapter = cardAdapter
            recyclerView.addItemDecoration(LinearSpaceDecoration())

            cardAdapter.setOnItemClick { pos, view1 ->
                replaceWithShareElement(
                    PlaylistFragment.newInstance(
                        viewModel.getPlaylistId(pos),
                        cardAdapter.currentList[pos].title
                    ),
                    PlaylistFragment.TAG,
                    view1
                )
            }
            cardAdapter.setOnCurrentListChanged { _, currentList ->
                lavEmpty.isVisible = currentList.isEmpty()
            }
            cardAdapter.setupSelection(recyclerView) {
                val hasSelection = cardAdapter.hasSelection()
                if (hasSelection != null) {
                    if (hasSelection) {
                        fab.show()
                    } else {
                        fab.hide()
                    }
                }
            }
            fab.setOnClickListener {
                cardAdapter.getSelected()?.size()?.apply {
                    if (this > 0) {
                        showDialog()
                    }
                }
            }
        }
    }

    private fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.tip_del_playlist))
            .setMessage(getString(R.string.tip_beyond_retrieve))
            .setPositiveButton(getString(R.string.enter)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
                viewModel.delete(cardAdapter.getSelected())
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.dismiss()
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.deletePlaylist.observe(viewLifecycleOwner, Observer {
            it.emitIfNotHandled({
                cardAdapter.clearSelection()
                showSnack(view, Message(id = R.string.tip_del_success))
                binding<FragmentMeBinding>()?.apply {
                    fab.hide()
                }
            }, {
                showSnack(view, Message(id = R.string.tip_del_fail))
            })
        })
        viewModel.playlistNames.observe(viewLifecycleOwner, Observer {
            cardAdapter.submitList(it)
        })
        viewModel.playlist.observe(viewLifecycleOwner, Observer { result ->
            result.emitIfNotHandled(
                {
                    hideProgressBar()
                    replace(
                        PlaylistFragment.newInstance(
                            it.data.playlistId,
                            it.data.playlistName
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
        binding<FragmentMeBinding>()?.apply {
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun hideProgressBar() {
        binding<FragmentMeBinding>()?.apply {
            progressBar.visibility = View.GONE
        }
    }
}