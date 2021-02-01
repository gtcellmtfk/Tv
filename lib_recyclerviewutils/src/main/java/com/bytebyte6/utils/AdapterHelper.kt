package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

interface AdapterHelper<T, V : DetailsViewHolder> {

    val adapter: RecyclerView.Adapter<V>

    val list: MutableList<T>

    /**拖拽模式*/
    var itemTouchHelper: ItemTouchHelper?

    /**多选模式*/
    var selectionTracker: SelectionTracker<Long>?

    var onItemClick: ((pos: Int, view: View) -> Unit)?

    var onItemLongClick: ((pos: Int, view: View) -> Boolean)?

    var onBind: ((pos: Int, view: View) -> Unit)?

    fun setupItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper = getItemTouchHelper(recyclerView)
    }

    fun setupSelectionTracker(
        recyclerView: RecyclerView,
        selectionObserver: SelectionTracker.SelectionObserver<Long>
    ) {
        selectionTracker = getSelectionTracker(
            recyclerView,
            selectionObserver
        )
    }

    fun clear() {
        list.clear()
        adapter.notifyDataSetChanged()
    }

    fun replace(collection: Collection<T>) {
        list.clear()
        list.addAll(collection)
        adapter.notifyDataSetChanged()
    }

    fun del(collection: Collection<T>) {
        collection.forEach {
            del(it)
        }
    }

    fun del(data: T) {
        val pos = list.indexOf(data)
        val remove = list.remove(data)
        if (remove && pos != -1) {
            adapter.notifyItemRemoved(pos)
        }
    }

    fun add(collection: Collection<T>) {
        val addAll = list.addAll(collection)
        if (addAll) {
            adapter.notifyItemRangeInserted(adapter.itemCount, collection.size)
        }
    }

    fun add(data: T) {
        val success = list.add(data)
        if (success) {
            adapter.notifyItemInserted(list.indexOf(data))
        }
    }

    fun swap(fromPos: Int, toPos: Int) {
        val fromValue = list[fromPos]
        val toValue = list[toPos]
        list[fromPos] = toValue
        list[toPos] = fromValue
        adapter.notifyItemMoved(fromPos, toPos)
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