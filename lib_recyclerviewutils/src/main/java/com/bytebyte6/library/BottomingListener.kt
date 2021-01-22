package com.bytebyte6.library

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BottomingListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val layoutManager: LinearLayoutManager =
            recyclerView.layoutManager as LinearLayoutManager
        val adapter = recyclerView.adapter
        if (adapter != null) {
            if (adapter.itemCount - 1 == layoutManager.findLastCompletelyVisibleItemPosition()
                && newState == RecyclerView.SCROLL_STATE_IDLE
            ) {
                onBottom()
            }
        }
    }

    abstract fun onBottom()
}