package com.bytebyte6.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.base.BaseFragment
import com.bytebyte6.base.BaseViewModel
import com.bytebyte6.logic.IpTvViewModel
import com.bytebyte6.logic.TAB
import com.bytebyte6.view.databinding.FragmentViewPagerBinding
import org.koin.android.viewmodel.ext.android.sharedViewModel

class ViewPagerFragment :
    BaseFragment<FragmentViewPagerBinding>(R.layout.fragment_view_pager) {

    companion object {
        const val TAG = "ViewPagerFragment"
    }

    private val viewModel: IpTvViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val singleLineAdapter = SingleLineAdapter()

        singleLineAdapter.setOnItemClick {
            val item = singleLineAdapter.currentList[it]
            showBottomSheetDialog(item)
        }

        binding?.apply {
            recyclerView.adapter = singleLineAdapter
//            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//                    val layoutManager: LinearLayoutManager =
//                        recyclerView.layoutManager as LinearLayoutManager
//                    if (layoutManager.findLastCompletelyVisibleItemPosition()
//                        == recyclerView.adapter!!.itemCount - 1
//                        && newState == RecyclerView.SCROLL_STATE_IDLE
//                    ) {
//
//                    }
//                }
//            })
        }

        viewModel.listLiveData(arguments!!.getInt(TAB))?.observe(this, Observer {
            singleLineAdapter.submitList(it as MutableList<*>)
        })
    }

    override fun initViewModel(): BaseViewModel {
        return viewModel
    }

    override fun initBinding(view: View): FragmentViewPagerBinding {
        return FragmentViewPagerBinding.bind(view)
    }

    private fun showBottomSheetDialog(any: Any) {
        val dialog = VideoDialog(arguments!!.getInt(TAB), any)
        dialog.show(fragmentManager!!, VideoDialog.TAG)
    }
}

class SingleLineAdapter :
    BaseAdapter<Any, SingleLineItemViewHolder>(AnyDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingleLineItemViewHolder {
        return SingleLineItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: SingleLineItemViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item: Any? = getItem(position)
        if (item == null || item.toString() == "") {
            holder.text.setText(R.string.home_other)
        } else {
            holder.text.text = item.toString()
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
                LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_selectable_list_item, parent, false)
            )
        }
    }
}
