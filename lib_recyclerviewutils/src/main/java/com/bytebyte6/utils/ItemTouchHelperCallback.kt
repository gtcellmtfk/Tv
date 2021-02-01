package com.bytebyte6.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ItemTouchHelperCallback<T, V : DetailsViewHolder>(
    private val adapterHelper: AdapterHelper<T, V>
) : ItemTouchHelper.Callback() {

    private var materialCardView: MaterialCardView? = null

    companion object {
        private const val DRAG_FLAGS = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        private const val SWIPE_FLAGS = 0
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(
            DRAG_FLAGS,
            SWIPE_FLAGS
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.bindingAdapterPosition
        val toPos = target.bindingAdapterPosition
        adapterHelper.swap(fromPos, toPos)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
            materialCardView = viewHolder.itemView as MaterialCardView
            materialCardView?.isDragged = true
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && materialCardView != null) {
            materialCardView?.isDragged = false
            materialCardView = null
        }
    }
}