package com.bytebyte6.view.me

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.base_ui.BaseShareFragment
import com.bytebyte6.base_ui.GridSpaceDecoration
import com.bytebyte6.base_ui.KEY_TRANS_NAME
import com.bytebyte6.view.*
import com.bytebyte6.view.databinding.FragmentPlayListBinding
import com.google.android.material.appbar.MaterialToolbar
import org.koin.android.viewmodel.ext.android.getViewModel

class PlaylistFragment : BaseShareFragment/*<FragmentPlayListBinding>*/(R.layout.fragment_play_list) {

    companion object {
        fun newInstance(
            playlistId: Long,
            title: String
        ): Fragment {
            return PlaylistFragment().apply {
                arguments = Bundle().apply {
                    putLong(KEY_PLAY_LIST_ID, playlistId)
                    putString(KEY_TITLE, title)
                    putString(KEY_TRANS_NAME, title)
                }
            }
        }

        const val TAG = "PlaylistFragment"
    }

    private val viewModel: MeViewModel? by lazy {
        var vm: MeViewModel? = null
        parentFragmentManager.findFragmentByTag(MeFragment.TAG)?.apply {
            vm = this.getViewModel()
        }
        vm
    }

    override fun initBinding(view: View): FragmentPlayListBinding {
        return FragmentPlayListBinding.bind(view).apply {

            setupToolbarArrowBack()

            toolbar.title = requireArguments().getString(KEY_TITLE)

            val adapter = ImageAdapter(this@PlaylistFragment) {
                viewModel?.fav(it)
            }
            adapter.setOnItemClick { pos, _ ->
                showVideoActivity(adapter.currentList[pos].videoUrl)
            }
            adapter.setOnBind { pos, _ ->
                viewModel?.searchLogo(pos)
            }
            recyclerView.adapter = adapter
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)

            load(adapter, toolbar)
        }
    }

    private fun load(adapter: ImageAdapter, toolbar: MaterialToolbar) {
        viewModel?.tvs(requireArguments().getLong(KEY_PLAY_LIST_ID))
            ?.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
                toolbar.subtitle = getString(R.string.total, adapter.itemCount)
            })
    }
}
