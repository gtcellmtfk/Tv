package com.bytebyte6.view.card

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bytebyte6.view.R
import com.google.android.material.card.MaterialCardView

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvBody: TextView = itemView.findViewById(R.id.tvBody)
    val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
    val ivIcon: ImageView = itemView.findViewById(R.id.ivIcon)
    companion object {
        fun create(parent: ViewGroup): CardViewHolder {
            return CardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card,
                    parent,
                    false
                )
            )
        }
    }
}

