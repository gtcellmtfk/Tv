package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bytebyte6.data.model.Image
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [Index(value = ["name"], unique = true)]
)

data class Country(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "countryId")
    @SerializedName("countryId")
    val countryId: Long = 0,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String = "",

    @SerializedName("images")
    @ColumnInfo(name = "images")
    var images: List<String> = emptyList()

) : Parcelable, Image {
    override val imageUrl: String
        get() = if (images.isNotEmpty()) images[0] else "https://tse4-mm.cn.bing.net/th/id/OIP.9IK3tQifJ2Zmtnm0GqlfOgHaHN?pid=Api&rs=1"
    override val title: String
        get() = name
    override val videoUrl: String
        get() = ""
}