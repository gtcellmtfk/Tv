package com.bytebyte6.view

import com.bytebyte6.view.R
import kotlin.random.Random


fun randomImage(): Int {
    return when (Random.Default.nextInt(9)) {
        0 -> R.drawable.ic_coronavirus
        1 -> R.drawable.ic_domain
        2 -> R.drawable.ic_favorite
        3 -> R.drawable.ic_lens
        4 -> R.drawable.ic_live_tv
        5 -> R.drawable.ic_sports_esports
        6 -> R.drawable.ic_storefront
        7 -> R.drawable.ic_weekend
        else -> R.drawable.ic_sentiment_very_dissatisfied
    }
}