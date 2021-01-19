package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.bytebyte6.data.model.Image
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
@Keep
data class Country(
    @PrimaryKey(autoGenerate = true)
    val countryId: Long = 0,
    override var name: String = "",
    var image: String = ""
) : Parcelable, Image {
    override var logo: String
        get() = image
        set(value) {}
    override var videoUrl: String
        get() = ""
        set(value) {}
    override var favorite: Boolean
        get() = false
        set(value) {}
    override val transitionName: String
        get() = this.name
    override var download: Boolean
        get() = false
        set(value) {}
}