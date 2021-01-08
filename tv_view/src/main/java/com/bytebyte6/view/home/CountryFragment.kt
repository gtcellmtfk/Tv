package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.ImageAdapter
import com.bytebyte6.view.R
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.search.SearchFragment
import com.bytebyte6.view.showVideoListFragment
import com.google.android.material.transition.Hold
import org.koin.android.viewmodel.ext.android.sharedViewModel

class CountryFragment : BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "CountryFragment"
        fun newInstance(): CountryFragment {
            return CountryFragment().apply {
                arguments = Bundle().apply {

                }
            }
        }
    }

    private val viewModel: HomeViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun initBinding(view: View): FragmentRecyclerViewBinding {
        return FragmentRecyclerViewBinding.bind(view).apply {

            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            val imageAdapter = ImageAdapter()
            recyclerView.adapter = imageAdapter
            recyclerView.layoutManager=GridLayoutManager(view.context,2)
            viewModel.cs.observe(viewLifecycleOwner, Observer {
                imageAdapter.submitList(it)
            })
            imageAdapter.setOnItemClick { pos, view ->
                showVideoListFragment(view, imageAdapter.currentList[pos].title)
            }
        }
    }


}