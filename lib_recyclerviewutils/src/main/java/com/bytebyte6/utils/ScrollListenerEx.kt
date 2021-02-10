package com.bytebyte6.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @param where 距离底部还有多少时加载
 * @param millis 多少毫秒内触发一次，防止反复横跳
 */
abstract class LoadMoreListener(
    private val where: Int = 1,
    private val millis: Long = 1000
) : RecyclerView.OnScrollListener() {

    private var last: Long = 0L
    private var vertical = false

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        load2(recyclerView)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        vertical = dy > 0
        load2(recyclerView)
    }

    private fun load2(recyclerView: RecyclerView) {
        val adapter = recyclerView.adapter ?: return

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        val lastCompletelyVisibleItemPosition =
            layoutManager.findLastCompletelyVisibleItemPosition()

        val itemCount = adapter.itemCount

        val threshold = itemCount - where

        if (lastCompletelyVisibleItemPosition >= threshold) {
            if (!vertical) {
                return
            }
            if (last == 0L) {
                load()
                return
            }
            if (System.currentTimeMillis() - last >= millis) {
                load()
                return
            }
        }
    }

    private fun load() {
        last = System.currentTimeMillis()
        onLoadMore()
        return
    }

    abstract fun onLoadMore()
}

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