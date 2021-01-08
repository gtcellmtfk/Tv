package com.bytebyte6.view.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.base.BaseAdapter
import com.bytebyte6.view.R
import com.google.android.material.card.MaterialCardView

class CardAdapter(
    var selectionTracker: SelectionTracker<Long>? = null,
    var itemTouchHelper: ItemTouchHelper? = null
) :
    BaseAdapter<String, CardViewHolder>(StringDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)
        holder.tvTitle.text = item
        holder.details.pos = position.toLong()
        holder.itemView.transitionName=item

        itemTouchHelper?.apply {
            holder.dragHandlerView.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    startDrag(holder)
                    true
                } else {
                    false
                }
            }
        }

        if (selectionTracker == null) {
            return
        }
        val selected = selectionTracker!!.isSelected(holder.details.selectionKey)
        holder.card.isChecked = selected
    }
}

object StringDiff : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == oldItem

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean = oldItem == oldItem
}

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val card: MaterialCardView = itemView.findViewById(R.id.cardView)
    val dragHandlerView = itemView.findViewById<ImageView>(R.id.iv_drag_handle)
    val details = Details()

    companion object {
        fun create(parent: ViewGroup): CardViewHolder {
            return CardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_string,
                    parent,
                    false
                )
            )
        }
    }
}

class Details : ItemDetailsLookup.ItemDetails<Long?>() {

    var pos: Long? = null

    override fun getSelectionKey(): Long? = pos

    override fun getPosition(): Int = if (pos == null) 0 else pos!!.toInt()
}

class DetailsLookup(private val recyclerView: RecyclerView) : ItemDetailsLookup<Long>() {
    override fun getItemDetails(e: MotionEvent): ItemDetails<Long?>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)
        if (view != null) {
            val viewHolder = recyclerView.getChildViewHolder(view)
            if (viewHolder is CardViewHolder) {
                return viewHolder.details
            }
        }
        return null
    }
}

class KeyProvider : ItemKeyProvider<Long>(SCOPE_MAPPED) {
    override fun getKey(position: Int): Long? {
        return position.toLong()
    }

    override fun getPosition(key: Long): Int {
        return key.toInt()
    }
}


class CardItemTouchHelperCallback(
    private val cardAdapter: CardAdapter,
    private var materialCardView: MaterialCardView? = null
) : ItemTouchHelper.Callback() {

    private val DRAG_FLAGS = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    private val SWIPE_FLAGS = 0


    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.adapterPosition
        val toPos = target.adapterPosition
        swapCards(fromPos, toPos, cardAdapter)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && viewHolder != null) {
            materialCardView = viewHolder.itemView as MaterialCardView
            materialCardView!!.isDragged = true
        } else if (actionState == ItemTouchHelper.ACTION_STATE_IDLE && materialCardView != null) {
            materialCardView!!.isDragged = false
            materialCardView = null
        }
    }
}

fun swapCards(fromPos: Int, toPos: Int, cardAdapter: CardAdapter) {
    val fromValue = cardAdapter.currentList[fromPos]
    val toValue = cardAdapter.currentList[toPos]
    val newlist = cardAdapter.currentList.toMutableList()
    newlist[fromPos] = toValue
    newlist[toPos] = fromValue
    cardAdapter.submitList(newlist)
    cardAdapter.notifyItemMoved(fromPos, toPos)
}