package com.bytebyte6.view.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.common.*
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.usecase.UpdateTvParam
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.adapter.ButtonClickListener
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.TvAdapter
import com.bytebyte6.view.setupOnBackPressedDispatcherBackToHome
import com.bytebyte6.view.setupToolbarMenuMode
import com.bytebyte6.view.toPlayer
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedDispatcherBackToHome()

        disEnabledSwipeRefreshLayout()

        setupToolbarMenuMode(getString(R.string.nav_fav), "")

        doOnExitTransitionEndOneShot {
            clearRecyclerView()
        }
        val adapter = TvAdapter(ButtonType.FAVORITE, object : ButtonClickListener {
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
                    showSnack(
                        view,
                        Message(
                            id = R.string.unbookmarked,
                            actionStringId = R.string.revocation
                        ),
                        View.OnClickListener { viewModel.restoreFavorite(tv) }
                    )
                }
            }
        })
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }
}

