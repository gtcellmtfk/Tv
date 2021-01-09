package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bytebyte6.base_ui.BaseFragment
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.showVideoListFragment
import org.koin.android.viewmodel.ext.android.sharedViewModel

class LanguageFragment :
    BaseFragment<FragmentRecyclerViewBinding>(R.layout.fragment_recycler_view) {

    companion object {
        const val TAG = "ViewPagerFragment"
        fun newInstance(): LanguageFragment {
            return LanguageFragment().apply {
                arguments = Bundle().apply { putInt(TAB, TAB_LANGUAGE) }
            }
        }
    }

    private val viewModel: HomeViewModel by sharedViewModel()

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        exitTransition = Hold()
//    }

    override fun initBinding(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view).apply {
//
//            postponeEnterTransition()
//
//            view.doOnPreDraw { startPostponedEnterTransition() }
//
//            val tab = requireArguments().getInt(TAB)

            val cardAdapter = CardAdapter()

            cardAdapter.setOnItemClick { pos, view1 ->
                val item = cardAdapter.currentList[pos]
                showVideoListFragment(view1, item.title)
            }

            recyclerView.adapter = cardAdapter

            cardAdapter.setupSelection(recyclerView)

            viewModel.lang.observe(viewLifecycleOwner, Observer {
                cardAdapter.submitList(it)
            })
        }


}
