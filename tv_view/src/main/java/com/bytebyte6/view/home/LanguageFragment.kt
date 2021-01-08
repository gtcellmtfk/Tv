package com.bytebyte6.view.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.Observer
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.Message
import com.bytebyte6.base.showToast
import com.bytebyte6.view.R
import com.bytebyte6.view.TAB
import com.bytebyte6.view.TAB_LANGUAGE
import com.bytebyte6.view.databinding.FragmentRecyclerViewBinding
import com.bytebyte6.view.showVideoListFragment
import com.google.android.material.transition.Hold
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun initBinding(view: View): FragmentRecyclerViewBinding =
        FragmentRecyclerViewBinding.bind(view).apply {

            postponeEnterTransition()
            view.doOnPreDraw { startPostponedEnterTransition() }

            val tab = requireArguments().getInt(TAB)

            val singleLineAdapter = CardAdapter()

            singleLineAdapter.setOnItemClick { pos, view1 ->
                val item = singleLineAdapter.currentList[pos]
                showVideoListFragment(view1,item)
            }

            recyclerView.adapter = singleLineAdapter

            val selectionTracker = SelectionTracker.Builder<Long>(
                "card_selection",
                recyclerView,
                KeyProvider(),
                DetailsLookup(recyclerView),
                StorageStrategy.createLongStorage()
            )
                .withSelectionPredicate(SelectionPredicates.createSelectAnything())
                .build()

            singleLineAdapter.selectionTracker=selectionTracker

            selectionTracker.addObserver(
                object : SelectionTracker.SelectionObserver<Long>(){
                    override fun onSelectionChanged() {
                        val size = selectionTracker.selection.size()
                        if (size >0){
                            showToast(Message(message = size.toString()))
                        }
                    }
                }
            )

            viewModel.listLiveData(tab)?.observe(viewLifecycleOwner, Observer {
                singleLineAdapter.submitList(it)
            })
        }


}
