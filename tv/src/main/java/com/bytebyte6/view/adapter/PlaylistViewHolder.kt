package com.bytebyte6.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bytebyte6.utils.DetailsViewHolder
import com.bytebyte6.view.R
import com.google.android.material.card.MaterialCardView

class PlaylistViewHolder(view: View) : DetailsViewHolder(view) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
    val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
    companion object {
        fun create(parent: ViewGroup): PlaylistViewHolder {
            return PlaylistViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card,
                    parent,
                    false
                )
            )
        }
    }
}

