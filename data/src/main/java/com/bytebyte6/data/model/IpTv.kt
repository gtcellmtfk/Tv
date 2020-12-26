package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "iptvs")
data class IpTv(
    var category: String?,
    var logo: String?,
    var name: String?,
    @PrimaryKey
    var url: String,
    @Embedded var country: Country?,
    var language: List<Language> = emptyList()
) : Parcelable {
    override fun toString(): String {
        return "IpTv(category=$category, logo=$logo, name=$name, url=$url, country=$country, language=$language)"
    }
}