package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

/**
 * 根布局必须实现Checkable接口，如：MaterialCardView
 */
interface AdapterHelper<T, V : DetailsViewHolder> {

    val adapter: RecyclerView.Adapter<V>

    /**多选模式*/
    var selectionTracker: SelectionTracker<Long>?

    var onItemClick: ((pos: Int, view: View) -> Unit)?

    var onItemLongClick: ((pos: Int, view: View) -> Boolean)?

    var onBind: ((pos: Int, view: View) -> Unit)?

    val list: MutableList<T>

    fun replace(list: List<T>) {
        this.list.clear()
        this.list.addAll(list)
        adapter.notifyDataSetChanged()
    }

    fun setupSelectionTracker(
        recyclerView: RecyclerView,
        selectionObserver: SelectionTracker.SelectionObserver<Long>
    ) {
        selectionTracker = getSelectionTracker(recyclerView, selectionObserver)
    }

    fun onHelperBindViewHolder(holder: V, position: Int) {
        onBind?.invoke(position, holder.itemView)
        onItemClick?.apply {
            holder.itemView.setOnClickListener {
                if (selectionTracker == null) {
                    this.invoke(position, holder.itemView)
                } else {
                    //当没有选中时才响应点击事件
                    if (!selectionTracker!!.hasSelection()) {
                        this.invoke(position, holder.itemView)
                    }
                }
            }
        }
        onItemLongClick?.apply {
            holder.itemView.setOnLongClickListener {
                this.invoke(position, holder.itemView)
            }
        }
        selectionTracker?.apply {
            holder.details.pos = position.toLong()
            val selected = isSelected(holder.details.selectionKey)
            if (holder.itemView is MaterialCardView) {
                holder.itemView.isChecked = selected
            }
        }
    }
}