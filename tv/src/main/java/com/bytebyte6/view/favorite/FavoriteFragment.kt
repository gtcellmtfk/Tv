package com.bytebyte6.view.favorite

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.viewmodel.FavoriteViewModel
import com.bytebyte6.common.doOnExitTransitionEndOneShot
import com.bytebyte6.utils.GridSpaceDecoration
import com.bytebyte6.utils.ListFragment
import com.bytebyte6.view.*
import com.bytebyte6.view.adapter.ButtonType
import com.bytebyte6.view.adapter.ImageAdapter
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
        val adapter = ImageAdapter(ButtonType.NONE)
            .apply {
            onItemClick = { pos, _: View ->
                toPlayer(currentList[pos].videoUrl)
            }
            onCurrentListChanged = { _, currentList ->
                binding?.emptyBox?.isVisible = currentList.isEmpty()
            }
        }

        imageClearHelper = adapter

        binding?.run {
            recyclerview.adapter = adapter
            recyclerview.layoutManager = GridLayoutManager(view.context, 2)
            recyclerview.addItemDecoration(GridSpaceDecoration())
            recyclerview.setHasFixedSize(true)
            recyclerview.itemAnimator = null
        }

        viewModel.allFav.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }
}

