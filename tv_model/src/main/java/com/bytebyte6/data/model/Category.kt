package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Ignore
import com.bytebyte6.common.randomColorByNightMode
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

object CategoryDiff : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.category == newItem.category
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}


@Parcelize
@Keep
data class Category(
    var category: String = ""
) : Parcelable {
    @IgnoredOnParcel
    @Ignore
    val color :Int = randomColorByNightMode()
    companion object {
        const val OTHER = "OTHER"
    }
}