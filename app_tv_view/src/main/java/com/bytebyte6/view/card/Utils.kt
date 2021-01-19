package com.bytebyte6.view.card

import android.os.Bundle
import android.view.View
import androidx.core.view.AccessibilityDelegateCompat
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate
import com.bytebyte6.view.R
import java.util.*

fun getSelectionTracker(
    recyclerView: RecyclerView,
    observer: SelectionTracker.SelectionObserver<Long>
): SelectionTracker<Long> {
    if (recyclerView.adapter == null) {
        throw IllegalArgumentException("recyclerview adapter can not be null,please set adapter first!")
    }
    val selectionTracker = SelectionTracker.Builder<Long>(
        UUID.randomUUID().toString(),
        recyclerView,
        KeyProvider(),
        DetailsLookup(recyclerView),
        StorageStrategy.createLongStorage()
    )
        .withSelectionPredicate(SelectionPredicates.createSelectAnything())
        .build()
    selectionTracker.addObserver(observer)
    return selectionTracker
}

fun <T,V : RecyclerView.ViewHolder> ListAdapter<T,V>.getItemTouchHelper(recyclerView: RecyclerView): ItemTouchHelper {
    val itemTouchHelper = ItemTouchHelper(
        CardItemTouchHelperCallback(this)
    )
    val delegate = object : RecyclerViewAccessibilityDelegate(recyclerView) {
        override fun getItemDelegate(): AccessibilityDelegateCompat {
            return getItemDelegate(recyclerView, this)
        }
    }
    recyclerView.setAccessibilityDelegateCompat(delegate)
    itemTouchHelper.attachToRecyclerView(recyclerView)
    return itemTouchHelper
}

fun <T,V : RecyclerView.ViewHolder> ListAdapter<T, V>.getItemDelegate(
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

fun <T,V : RecyclerView.ViewHolder> ListAdapter<T,V>.swapCards(fromPos: Int, toPos: Int) {
    val fromValue = currentList[fromPos]
    val toValue = currentList[toPos]
    val newlist = currentList.toMutableList()
    newlist[fromPos] = toValue
    newlist[toPos] = fromValue
    submitList(newlist)
    notifyItemMoved(fromPos, toPos)
}