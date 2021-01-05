package com.bytebyte6.view.me

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.view.KEY_PLAY_LIST_ID
import com.bytebyte6.view.KEY_TITLE
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentPlayListBinding
import com.bytebyte6.view.setupToolbar
import com.bytebyte6.view.video.VideoActivity
import com.bytebyte6.view.video.VideoAdapter
import org.koin.android.viewmodel.ext.android.sharedViewModel

class PlaylistFragment : BaseFragment<FragmentPlayListBinding>(R.layout.fragment_play_list) {

    companion object {
        fun newInstance(playlistId: Long,title:String): Fragment {
            return PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_PLAY_LIST_ID, playlistId)
                    putString(KEY_TITLE, title)
                }
            }
        }

        const val TAG = "PlaylistFragment"
    }

    private val viewModel: MeViewModel by sharedViewModel()

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? {
        return viewModel
    }

    override fun initBinding(view: View): FragmentPlayListBinding {
        return FragmentPlayListBinding.bind(view).apply {
            setupToolbar()

            toolbar.title=requireArguments().getString(KEY_TITLE)

            val adapter = VideoAdapter()
            adapter.setOnItemClick { pos, _ ->
                startActivity(Intent(context, VideoActivity::class.java).apply {
                    putExtra(Tv.TAG, adapter.currentList[pos])
                })
            }
            recyclerView.adapter = adapter
            viewModel.tvs(requireArguments().getLong(KEY_PLAY_LIST_ID))
                .observe(viewLifecycleOwner, Observer {
                    adapter.submitList(it)
                    toolbar.subtitle = getString(R.string.total,adapter.itemCount)
                })
        }
    }
}
