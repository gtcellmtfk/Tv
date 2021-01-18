package com.bytebyte6.view.card

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.core.view.isVisible
import androidx.recyclerview.selection.Selection
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.bytebyte6.base_ui.BaseAdapter
import com.bytebyte6.data.model.Card
import com.bytebyte6.view.R
import com.bytebyte6.view.home.randomImage

class CardAdapter(
    /**多选模式必须设置*/
    private var selectionTracker: SelectionTracker<Long>? = null,
    /**拖拽模式必须设置*/
    private var itemTouchHelper: ItemTouchHelper? = null,
    /**是否启用拖拽模式*/
    private var drag: Boolean = false
) : BaseAdapter<Card, CardViewHolder>(CardDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder.create(parent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val item = getItem(position)

        holder.apply {
            //重建后的recyclerview Item是没有transName的 所以在onbind要重新赋值一遍 动画效果才会有~~
            itemView.transitionName = item.title
            tvTitle.text = item.title
            details.pos = position.toLong()
            dragHandlerView.isVisible = drag
            tvBody.text = item.body
            ivIcon.setImageResource(randomImage())
            if (getItem(position).outline) {
                cardView.apply {
                    strokeWidth = 4
                    strokeColor = item.color
                    radius = item.radius.toFloat()
                    setCardBackgroundColor(
                        ContextCompat.getColor(
                            holder.itemView.context,
                            R.color.rootLayoutBackgroundColor
                        )
                    )
                }
            } else {
                cardView.apply {
                    strokeWidth = 0
                    strokeColor = 0
                    radius = item.radius.toFloat()
                    setCardBackgroundColor(item.color)
                }
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
            holder.itemView.setOnClickListener {
                if (!hasSelection()) {
                    getOnItemClick()?.invoke(position, it)
                }
            }
        }
    }

    /*拖拽模式必须设置*/
    fun setupDrag(recyclerView: RecyclerView) {
        itemTouchHelper = ItemTouchHelper(
            CardItemTouchHelperCallback(
                this
            )
        )
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

    fun getSelected(): Selection<Long>? = selectionTracker?.selection

    fun hasSelection(): Boolean? = selectionTracker?.hasSelection()

    fun clearSelection() {
        selectionTracker?.apply {
            clearSelection()
        }
    }

    /**多选模式必须设置*/
    fun setupSelection(recyclerView: RecyclerView, onSelectionChanged: (() -> (Unit))? = null) {
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
                    onSelectionChanged?.invoke()
                }

                override fun onItemStateChanged(key: Long, selected: Boolean) {

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