package com.bytebyte6.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BottomingListener : RecyclerView.OnScrollListener() {
    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)

        val adapter = recyclerView.adapter ?: return

        val layoutManager = recyclerView.layoutManager

        if (layoutManager is LinearLayoutManager) {
            if (adapter.itemCount - 1 == layoutManager.findLastCompletelyVisibleItemPosition()
                && newState == RecyclerView.SCROLL_STATE_IDLE
            ) {
                onBottom()
            }
        }
    }

    abstract fun onBottom()
}

fun RecyclerView.doSomethingOnIdle(onIdle: (first: Int, last: Int) -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                onIdle.invoke(
                    layoutManager.findFirstVisibleItemPosition(),
                    layoutManager.findLastVisibleItemPosition()
                )
            }
        }
    })
}