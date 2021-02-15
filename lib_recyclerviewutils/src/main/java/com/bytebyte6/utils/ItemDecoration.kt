package com.bytebyte6.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State
import com.bytebyte6.common.dp16
import com.bytebyte6.common.dp8

class LinearSpaceDecoration(
    private val start: Int = dp16,
    private val top: Int = dp8,
    private val end: Int = start,
    private val bottom: Int = top
) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val count: Int? = parent.adapter?.itemCount
        val layoutManager = parent.layoutManager as LinearLayoutManager
        val orientation = layoutManager.orientation
        if (count != null) {
            val pos = parent.getChildLayoutPosition(view)
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                val end2 = if (pos == count - 1) end else 0
                outRect.set(start, top, end2, bottom)
            } else {
                val bottom2 = if (pos == count - 1) bottom else 0
                outRect.set(start, top, end, bottom2)
            }
        }
    }
}

/**
 * 没有处理横向情况
 * {@link GridSpaceDecoration2}
 */
class GridSpaceDecoration(
    private val distance: Int = dp8,
    private val span: Int = 2
) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val itemCount: Int? = parent.adapter?.itemCount
        if (itemCount != null) {
            val pos = parent.getChildLayoutPosition(view)

            if (itemCount <= span) {
                //只有一行
                if (pos == 0) {
                    outRect.set(distance, distance, distance, distance)
                } else {
                    outRect.set(0, distance, distance, distance)
                }
                return
            }

            val top: Int = if (pos < span) {
                //第一行
                this.distance
            } else {
                0
            }

            val start: Int = if (pos % span == 0) {
                //第一列
                this.distance
            } else {
                0
            }

            outRect.set(start, top, distance, distance)
        }
    }
}

class GridSpaceDecoration2(
    private val distance: Int = dp8,
    private val span: Int = 4
) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val itemCount: Int? = parent.adapter?.itemCount
        if (itemCount != null) {
            val pos = parent.getChildLayoutPosition(view)

            if (itemCount <= span) {
                if (pos == 0) {
                    outRect.set(distance, distance, distance, distance)
                } else {
                    outRect.set(distance, 0, distance, distance)
                }
                return
            }

            val start: Int = if (pos < span) {
                this.distance
            } else {
                0
            }

            val top: Int = if (pos % span == 0) {
                this.distance
            } else {
                0
            }

            outRect.set(start, top, distance, distance)
        }
    }
}


