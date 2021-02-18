package com.bytebyte6.view.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.common.SimpleDrawerListener
import com.bytebyte6.common.doOnExitTransitionEndOneShot
import com.bytebyte6.common.isSuccess
import com.bytebyte6.common.longSnack
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.*
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.TvAdapter
import com.bytebyte6.viewmodel.FavoriteViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class FavoriteFragment : ListFragment() {

    companion object {
        const val TAG = "FavoriteFragment"
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }

    private val viewModel: FavoriteViewModel by viewModel()

    private val listener = object : SimpleDrawerListener() {
        override fun onDrawerClosed(drawerView: View) {
            binding?.emptyBox?.resumeAnimation()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedDispatcherBackToHome()

        disEnabledSwipeRefreshLayout()

        setupToolbarMenuMode(getString(R.string.nav_fav), "") {
            binding?.emptyBox?.pauseAnimation()
        }
        DrawerHelper.getInstance(requireActivity())?.addDrawerListener(listener)

        doOnExitTransitionEndOneShot {
            clearRecyclerView()
        }
        val adapter = TvAdapter(object : ButtonClickListener {
            override fun onClick(position: Int, tv: Tv) {
                viewModel.favorite(position, tv)
            }
        }).apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].url)
            }
            onCurrentListChanged = { _, currentList ->
                binding?.emptyBox?.isVisible = currentList.isEmpty()
            }
        }

        imageClearHelper = adapter

        binding?.run {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridLayoutManager(view.context, 2)
            recyclerView.addItemDecoration(GridSpaceDecoration())
            recyclerView.setHasFixedSize(true)
            recyclerView.itemAnimator = null
        }

        viewModel.allFav.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
        viewModel.cancelResult.observe(viewLifecycleOwner, Observer {
            it.isSuccess()?.apply {
                if (pos != -1) {
                    view.longSnack(R.string.unbookmarked) {
                        setAction(R.string.revocation) {
                            viewModel.restoreFavorite(tv)
                        }
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        DrawerHelper.getInstance(requireActivity())?.removeDrawerListener(listener)
    }

    override fun onLoadMore() = Unit

    override fun onRefresh() = Unit
}

