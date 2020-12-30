package com.bytebyte6.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModelDelegate
import com.bytebyte6.data.model.Category
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.Languages
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.logic.TAB
import com.bytebyte6.view.databinding.FragmentViewPagerBinding
import com.google.android.material.transition.Hold
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import org.koin.android.viewmodel.ext.android.sharedViewModel
import kotlin.random.Random

class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(R.layout.fragment_view_pager) {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    private val viewModel: IpTvViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
//        exitTransition = MaterialElevationScale(/* growing= */ false)
//        reenterTransition = MaterialElevationScale(/* growing= */ true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tab = requireArguments().getInt(TAB)

        val singleLineAdapter = SingleLineAdapter()

        singleLineAdapter.setOnItemClick { pos, view1 ->
            val item = singleLineAdapter.currentList[pos]
            viewModel.tab = tab
            viewModel.clickItem = item
//            if (Random.Default.nextBoolean()) {
//                showBottomSheetDialog()
//            } else {
                showVideoListFragment(view1)
//            }
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

    private val dialog = VideoDialog()

    private fun showBottomSheetDialog() {
        dialog.show(parentFragmentManager, VideoDialog.TAG)
    }

    private fun showVideoListFragment(view: View) {
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.main_container,VideoListFragment(),VideoListFragment.TAG)
//            .addToBackStack(TAG)
//            .commit()
        view.transitionName = getString(R.string.share)
        val extras = FragmentNavigatorExtras(view to getString(R.string.share))
        findNavController().navigate(
            R.id.action_homeFragment_to_videoListFragment,
            null,
            null,
            extras
        )
    }
}

class SingleLineAdapter :
    BaseAdapter<Any, SingleLineItemViewHolder>(AnyDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
        return SingleLineItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (val item: Any = getItem(position)) {
            is Country -> {
                holder.text.text = item.countryName //.plus(" ${item.count}")
            }
            is Category -> {
                if (item.category.isEmpty()) {
                    val s =
                        holder.text.context.getString(R.string.home_other) //.plus(" ${item.count}")
                    holder.text.text = s
                } else {
                    holder.text.text = item.category //.plus(" ${item.count}")
                }
            }
            is Languages -> {
                val stringBuilder = StringBuilder()
                item.languages.forEachIndexed { index, language ->
                    stringBuilder.append(language.languageName)
                    if (index != item.languages.size - 1) {
                        stringBuilder.append(" , ")
                    }
                }
                if (stringBuilder.isEmpty()) {
                    val s =
                        holder.text.context.getString(R.string.home_other) //.plus(" ${item.count}")
                    holder.text.text = s
                } else {
                    holder.text.text = stringBuilder.toString() //.plus(" ${item.count}")
                }
            }
        }
    }
}

object AnyDiff : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == oldItem

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean = oldItem == oldItem
}

class SingleLineItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val text: TextView = itemView.findViewById(android.R.id.text1)

    companion object {
        fun create(parent: ViewGroup): SingleLineItemViewHolder {
            return SingleLineItemViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    android.R.layout.simple_selectable_list_item,
                    parent,
                    false
                )
            )
        }
    }
}
