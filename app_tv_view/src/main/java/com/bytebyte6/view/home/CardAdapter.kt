package com.bytebyte6.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.isVisible
import androidx.recyclerview.selection.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.data.model.Card
import com.bytebyte6.view.R
import com.google.android.material.card.MaterialCardView

object StringDiff : DiffUtil.ItemCallback<Card>() {
    override fun areItemsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title

    override fun areContentsTheSame(
        oldItem: Card,
        newItem: Card
    ): Boolean = oldItem.title == oldItem.title && oldItem.body == oldItem.body
}


class CardAdapter(
    var selectionTracker: SelectionTracker<Long>? = null,
    var itemTouchHelper: ItemTouchHelper? = null,
    var drag: Boolean = false
) :
    BaseAdapter<Card, CardViewHolder>(StringDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            tvTitle.text = item.title
            details.pos = position.toLong()
            itemView.transitionName = item.title
            dragHandlerView.isVisible = drag

            if (getItem(position).outline) {
                cardView.strokeWidth = 2
                cardView.elevation = 0f
            }
        }

        itemTouchHelper?.apply {
            holder.dragHandlerView.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    startDrag(holder)
                    true
                } else {
                    false
                }
            }
        }

        selectionTracker?.apply {
            val selected = isSelected(holder.details.selectionKey)
            holder.cardView.isChecked = selected
        }
    }

    fun setupDrag(recyclerView: RecyclerView) {
        itemTouchHelper = ItemTouchHelper(CardItemTouchHelperCallback(this))
        val delegate = object : RecyclerViewAccessibilityDelegate(recyclerView) {
            override fun getItemDelegate(): AccessibilityDelegateCompat {
                return getItemDelegate(recyclerView, this)
            }
        }
        recyclerView.setAccessibilityDelegateCompat(delegate)
        itemTouchHelper!!.attachToRecyclerView(recyclerView)
    }

    private fun getItemDelegate(
        recyclerView: RecyclerView,
        recyclerViewAccessibilityDelegate: RecyclerViewAccessibilityDelegate
    ): RecyclerViewAccessibilityDelegate.ItemDelegate {
        return object :
            RecyclerViewAccessibilityDelegate.ItemDelegate(recyclerViewAccessibilityDelegate) {
            override fun onInitializeAccessibilityNodeInfo(
                host: View,
                info: AccessibilityNodeInfoCompat
            ) {
                super.onInitializeAccessibilityNodeInfo(host, info)
                val pos = recyclerView.getChildLayoutPosition(host)
                if (pos != 0) {
                    info.addAction(
                        AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                            R.id.move_card_up_action,
                            recyclerView.context.getString(R.string.cat_card_action_move_up)
                        )
                    )
                }
                if (pos != (itemCount - 1)) {
                    info.addAction(
                        AccessibilityNodeInfoCompat.AccessibilityActionCompat(
                            R.id.move_card_down_action,
                            recyclerView.context.getString(R.string.cat_card_action_move_down)
                        )
                    )
                }
            }

            override fun performAccessibilityAction(
                host: View,
                action: Int,
                args: Bundle?
            ): Boolean {
                val fromPos = recyclerView.getChildLayoutPosition(host)
                if (action == R.id.move_card_down_action) {
                    swapCards(fromPos, fromPos + 1)
                    return true
                } else if (action == R.id.move_card_up_action) {
                    swapCards(fromPos, fromPos - 1)
                    return true
                }

                return super.performAccessibilityAction(host, action, args)
            }
        }
    }

    private val selectedPos = mutableListOf<Int>()

    fun getSelectedPos(): List<Int> = selectedPos

    fun setupSelection(recyclerView: RecyclerView) {
        if (recyclerView.adapter == null) {
            recyclerView.adapter = this
        }
        selectionTracker = SelectionTracker.Builder<Long>(
            "card_selection",
            recyclerView,
            KeyProvider(),
            DetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        )
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
        selectionTracker!!.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {

                }

                override fun onItemStateChanged(key: Long, selected: Boolean) {
                    if (selected) {
                        selectedPos.add(key.toInt())
                    } else {
                        selectedPos.remove(key.toInt())
                    }
                }

                override fun onSelectionRefresh() {

                }

                override fun onSelectionRestored() {

                }
            }
        )
    }

    fun swapCards(fromPos: Int, toPos: Int) {
        val fromValue = currentList[fromPos]
        val toValue = currentList[toPos]
        val newlist = currentList.toMutableList()
        newlist[fromPos] = toValue
        newlist[toPos] = fromValue
        submitList(newlist)
        notifyItemMoved(fromPos, toPos)
    }
}

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
    val dragHandlerView: ImageView = itemView.findViewById(R.id.iv_drag_handle)
    val details = Details()

    companion object {
        fun create(parent: ViewGroup): CardViewHolder {
            return CardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card,
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
    private val cardAdapter: CardAdapter
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
        return makeMovementFlags(DRAG_FLAGS, SWIPE_FLAGS)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPos = viewHolder.bindingAdapterPosition
        val toPos = target.bindingAdapterPosition
        cardAdapter.swapCards(fromPos, toPos)
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