package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    @SerializedName("code")
    val countryCode: String?,
    @SerializedName("name")
    val countryName: String?
) : Parcelable{
    override fun toString(): String {
        return "Country(countryCode=$countryCode, countryName=$countryName)"
    }
}