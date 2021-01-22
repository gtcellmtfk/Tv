package com.bytebyte6.library

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val details= Details()
}