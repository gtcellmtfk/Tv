package com.bytebyte6.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class DetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val details= Details()
}