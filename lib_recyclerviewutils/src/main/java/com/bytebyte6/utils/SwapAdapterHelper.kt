package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

interface SwapAdapterHelper<T, V : DetailsViewHolder> : AdapterHelper<T,V>{

    val list: MutableList<T>

    /**拖拽模式*/
    var itemTouchHelper: ItemTouchHelper?

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

    fun replace(list:List<T>){
        this.list.clear()
        this.list.addAll(list)
        adapter.notifyDataSetChanged()
    }
}