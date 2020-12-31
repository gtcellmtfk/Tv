package com.bytebyte6.view

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.navigation.NavDestinationBuilder
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.logic.TAB
import com.bytebyte6.view.databinding.FragmentViewPagerBinding
import com.google.android.material.transition.Hold
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.random.Random

class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(R.layout.fragment_view_pager) {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    private val viewModel: IpTvViewModel by sharedViewModel()

    private val dialog = VideoDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        val tab = requireArguments().getInt(TAB)

        val singleLineAdapter = SingleLineAdapter()

        singleLineAdapter.setOnItemClick { pos, view1 ->
            val item = singleLineAdapter.currentList[pos]
            viewModel.tab = tab
            viewModel.clickItem = item
            if (Random.Default.nextBoolean()) {
                showBottomSheetDialog()
            } else {
                showVideoListFragment(view1)
            }
        }

        binding?.apply {
            recyclerView.adapter = singleLineAdapter
        }

        viewModel.listLiveData(tab)?.observe(viewLifecycleOwner, Observer {
            singleLineAdapter.submitList(it as MutableList<*>)
        })
    }

    override fun initBaseViewModelDelegate(): BaseViewModelDelegate? = viewModel

    override fun initBinding(view: View): FragmentViewPagerBinding =
        FragmentViewPagerBinding.bind(view)

    private fun showBottomSheetDialog() {
        dialog.show(parentFragmentManager, VideoDialog.TAG)
    }

    private fun showVideoListFragment(view: View) {
        val extras = FragmentNavigatorExtras(view to view.transitionName)

        val d= HomeFragmentDirections.actionHomeFragmentToVideoListFragment(view.transitionName)

        findNavController().navigate(d,extras)
    }
}
