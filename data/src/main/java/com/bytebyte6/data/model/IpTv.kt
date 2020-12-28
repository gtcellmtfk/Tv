package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "iptvs")
data class IpTv(
    val category: String = "",
    val logo: String = "",
    val name: String = "",
    @PrimaryKey
    val url: String,
    @Embedded var country: Country = Country(),
    val language: List<Language> = emptyList()
) : Parcelable {
    companion object {
        const val TAG = "IpTv"
    }
}

object IpTvDiff : DiffUtil.ItemCallback<IpTv>() {
    override fun areItemsTheSame(oldItem: IpTv, newItem: IpTv): Boolean = oldItem.url == oldItem.url

    override fun areContentsTheSame(oldItem: IpTv, newItem: IpTv): Boolean = oldItem == oldItem
}