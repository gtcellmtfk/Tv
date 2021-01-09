package com.bytebyte6.view.me

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base.EventObserver
import com.bytebyte6.base_ui.Message
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base_ui.showSnack
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentMeBinding
import com.bytebyte6.view.home.CardAdapter
import com.bytebyte6.view.replace
import com.bytebyte6.view.replaceWithShareElement
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

    private val cardAdapter = CardAdapter()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        exitTransition=Hold()
//    }

    override fun initBinding(view: View): FragmentMeBinding {
        return FragmentMeBinding.bind(view).apply {

//            postponeEnterTransition()
//
//            view.doOnPreDraw { startPostponedEnterTransition() }

            setupToolbar(requireActivity())

            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/m3u")
                    true
                }
            }

            recyclerView.adapter = cardAdapter
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
            cardAdapter.setupDrag(recyclerView)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.playlistNames.observe(viewLifecycleOwner, Observer {
            cardAdapter.submitList(it)
            binding?.apply {
                lavEmpty.isVisible = cardAdapter.itemCount == 0
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
                    showSnack(view,
                        Message(id = (R.string.tip_parse_file_error))
                    )
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