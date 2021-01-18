package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
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
@Keep
data class Country(
    @PrimaryKey(autoGenerate = true)
    val countryId: Long = 0,
    var name: String = "",
    var image: String = ""
) : Parcelable, Image {
    override var imageUrl: String
        get() = image
        set(value) {}
    override var title: String
        get() = name
        set(value) {}
    override var videoUrl: String
        get() = ""
        set(value) {}
    override var love: Boolean
        get() = false
        set(value) {}
}