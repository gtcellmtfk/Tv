/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bytebyte6.base_ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State

class LinearSpaceDecoration(
    private val start: Int = dp16,
    private val top: Int = dp8,
    private val end: Int = start,
    private val bottom: Int = top
) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val count: Int? = parent.adapter?.itemCount
        if (count != null) {
            val pos = parent.getChildLayoutPosition(view)
            val bottom2 = if (pos == count - 1) bottom else 0
            outRect.set(start, top, end, bottom2)
        }
    }
}

class GridSpaceDecoration(
    private val start: Int = dp8,
    private val top: Int = start,
    private val end: Int = start,
    private val bottom: Int = start,
    private val span: Int = 2
) : ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
        val itemCount: Int? = parent.adapter?.itemCount
        if (itemCount != null) {
            val pos = parent.getChildLayoutPosition(view)

            if (itemCount <= span ) {
                //只有一行
                outRect.set(start, top, end, bottom)
                return
            }

            val start: Int
            val top: Int

            top = if (pos < span) {
                //第一行
                this.top
            } else {
                0
            }

            start = if (pos % span == 0) {
                //第一列
                this.start
            } else {
                0
            }

            outRect.set(start, top, end, bottom)
        }
    }
}

