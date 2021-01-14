package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.bytebyte6.data.model.Image
import com.bytebyte6.data.model.Language
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [
        Index(value = ["tvId"]),
        Index(value = ["url"] , unique = true)
    ]
)
@Keep
data class Tv(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tvId")
    var tvId: Long = 0,

    @ColumnInfo(name = "url")
     var url: String = "",

    @ColumnInfo(name = "category")
    var category: String = "",

    @ColumnInfo(name = "logo")
    var logo: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "language")
    var language: List<Language> = emptyList(),

    @Ignore
    var country: Country = Country(),

    @ColumnInfo(name = "countryId")
    var countryId: Long = 0,

    @ColumnInfo(name = "countryName")
    var countryName: String = country.name

) : Parcelable , Image{

    override val title: String
        get() = name

    override val imageUrl: String
        get() = logo

    override val videoUrl: String
        get() = url
}
