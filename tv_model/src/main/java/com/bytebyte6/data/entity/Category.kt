package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.bytebyte6.common.randomColorByNightMode
import com.google.gson.annotations.Expose
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
@Entity
data class Category(
    @PrimaryKey
    var category: String = "",
    var categoryChinese: String = ""
) : Parcelable {
    companion object {
        const val OTHER = "Other"
    }
}