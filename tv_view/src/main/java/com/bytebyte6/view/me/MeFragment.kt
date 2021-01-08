package com.bytebyte6.view.me

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base.Message
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.showSnack
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentMeBinding
import com.bytebyte6.view.home.CardAdapter
import com.bytebyte6.view.home.CardItemTouchHelperCallback
import com.bytebyte6.view.home.swapCards
import com.bytebyte6.view.replace
import com.bytebyte6.view.setupToolbar
import org.koin.android.viewmodel.ext.android.viewModel

class MeFragment : BaseFragment<FragmentMeBinding>(R.layout.fragment_me) {

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

    private val stringAdapter = CardAdapter()

    override fun initBinding(view: View): FragmentMeBinding {
        return FragmentMeBinding.bind(view).apply {
            setupToolbar(requireActivity())

            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/m3u")
                    true
                }
            }

            recyclerView.adapter = stringAdapter
            stringAdapter.setOnItemClick { pos, _ ->
                replace(
                    PlaylistFragment.newInstance(
                        viewModel.getPlaylistId(pos),
                        stringAdapter.currentList[pos]
                    ),
                    PlaylistFragment.TAG
                )
            }
            val itemTouchHelper = ItemTouchHelper(CardItemTouchHelperCallback(stringAdapter))
            stringAdapter.itemTouchHelper = itemTouchHelper
            recyclerView.setAccessibilityDelegateCompat(
                object : RecyclerViewAccessibilityDelegate(recyclerView) {
                    override fun getItemDelegate(): AccessibilityDelegateCompat {
                        return object : ItemDelegate(this) {
                            override fun onInitializeAccessibilityNodeInfo(
                                host: View,
                                info: AccessibilityNodeInfoCompat
                            ) {
                                super.onInitializeAccessibilityNodeInfo(host, info)
                                val pos = recyclerView.getChildLayoutPosition(host)
                                if (pos != 0) {
                                    info.addAction(
                                        AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                                            R.id.move_card_up_action,
                                            getString(R.string.cat_card_action_move_up)
                                        )
                                    )
                                }
                                if (pos != (stringAdapter.itemCount - 1)) {
                                    info.addAction(
                                        AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                                            R.id.move_card_down_action,
                                            getString(R.string.cat_card_action_move_down)
                                        )
                                    )
                                }
                            }

                            override fun performAccessibilityAction(
                                host: View,
                                action: Int,
                                args: Bundle?
                            ): Boolean {
                                val fromPos = recyclerView.getChildLayoutPosition(host)
                                if (action == R.id.move_card_down_action) {
                                    swapCards(fromPos, fromPos + 1, stringAdapter)
                                    return true
                                } else if (action == R.id.move_card_up_action) {
                                    swapCards(fromPos, fromPos - 1, stringAdapter)
                                    return true
                                }

                                return super.performAccessibilityAction(host, action, args)
                            }
                        }
                    }
                }
            )
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlistNames.observe(viewLifecycleOwner, Observer {
            stringAdapter.submitList(it)
            binding?.apply {
                lavEmpty.isVisible = stringAdapter.itemCount == 0
            }
        })
        viewModel.playlistId.observe(viewLifecycleOwner, EventObserver { result ->
            when (result) {
                is Result.Success -> {
                    hideProgressBar()
                    replace(
                        PlaylistFragment.newInstance(result.data, viewModel.getPlaylistName()),
                        PlaylistFragment.TAG
                    )
                }
                is Result.Error -> {
                    hideProgressBar()
                    showSnack(view, Message(id = (R.string.tip_parse_file_error)))
                }
                Result.Loading -> {
                    showProgressBar()
                }
            }
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