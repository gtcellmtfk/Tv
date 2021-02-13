package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
@Keep
data class Country(
    @PrimaryKey(autoGenerate = true)
    var countryId: Long = 0,
    var name: String = "",
    var code: String = "",
    var image: String = ""
) : Parcelable {
    companion object {
        const val UNKOWN = "UNKOWN"
    }
}

object CountryDiff : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem.countryId == newItem.countryId
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
        return oldItem == newItem
    }
}