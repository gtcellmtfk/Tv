package com.bytebyte6.view.me

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.Message
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.showSnack
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentMeBinding
import com.bytebyte6.view.home.StringAdapter
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
        registerForActivityResult(getContent) {
            viewModel.parseM3u(it)
        }
    }

    private val stringAdapter = StringAdapter()

    override fun initBinding(view: View): FragmentMeBinding {
        return FragmentMeBinding.bind(view).apply {
            setupToolbar(requireActivity())

            toolbar.apply {
                setOnMenuItemClickListener {
                    launcher.launch("*/*")
                    true
                }
            }

            recyclerView.adapter = stringAdapter
            stringAdapter.setOnItemClick { pos, _ ->
                replace(
                    PlaylistFragment.newInstance(viewModel.getPlaylistId(pos),stringAdapter.getData(pos)),
                    PlaylistFragment.TAG
                )
            }
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
        viewModel.playlistId.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Result.Success -> {
                    hideProgressBar()
                    replace(PlaylistFragment.newInstance(it.data,viewModel.getPlaylistName()), PlaylistFragment.TAG)
                }
                is Result.Loading -> {
                    showProgressBar()
                }
                is Result.Error -> {
                    hideProgressBar()
                    showSnack(view, Message(id = (R.string.tip_parse_file_error)))
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