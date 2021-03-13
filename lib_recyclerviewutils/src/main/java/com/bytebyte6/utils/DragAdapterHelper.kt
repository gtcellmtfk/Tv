package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

/**
 * 拖拽模式实现，根布局必须实现Checkable接口，如：MaterialCardView
 */
interface DragAdapterHelper<T, V : DetailsViewHolder> : AdapterHelper<T, V> {
    /**拖拽模式设置*/
    var itemTouchHelper: ItemTouchHelper?

    /**拖拽模式设置*/
    fun setupItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper = getItemTouchHelper(recyclerView)
    }

    fun swap(fromPos: Int, toPos: Int) {
        val fromValue = list[fromPos]
        val toValue = list[toPos]
        list[fromPos] = toValue
        list[toPos] = fromValue
        adapter.notifyItemMoved(fromPos, toPos)
    }
}