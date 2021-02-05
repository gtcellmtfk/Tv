package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Ignore
import com.bytebyte6.common.randomColorByNightMode
import kotlinx.android.parcel.Parcelize
import kotlin.random.Random

@Parcelize
@Keep
data class Category(
    var category: String = "",
    @Ignore var content: String = Nausea[Random.Default.nextInt(22)],
    @Ignore override val color: Int = randomColorByNightMode(),
    @Ignore override val radius: Int = 10,
    @Ignore override val outline: Boolean = Random.Default.nextBoolean()
) : Parcelable, Card {

    companion object{
        const val OTHER="OTHER"
    }

    override val title: String
        get() = category

    override val body: String
        get() = content

    override val transitionName: String
        get() = category
}

val Nausea = mutableListOf(
    "Never say die.",
    "Just do it.",
    "Believe in yourself.",
    "Learn and live.",
    "Life is but a span.",
    "Learn not and know not.",
    "Knowledge is power.",
    "hang on to your dreams.",
    "Nurture passes nature.",
    "Nothing is impossible.",
    "Justice has long arms.",
    "Life is not all roses.",
    "Better late than never.",
    "Bad times make a good man.",
    "want it more that anything.",
    "Action speak louder than words.",
    "Never to old to learn.",
    "Believe that god is fair.",
    "Kings have long arms.",
    "Let bygones be bygones.",
    "Sow nothing， reap nothing.",
    "Learn to walk before you run."
)