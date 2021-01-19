package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.*
import com.bytebyte6.data.model.Card
import com.bytebyte6.data.model.Image
import com.bytebyte6.data.model.Language
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [
        Index(value = ["tvId"]),
        Index(value = ["url"], unique = true)
    ]
)
@Keep
data class Tv(
    @PrimaryKey(autoGenerate = true)
    var tvId: Long = 0,
    var url: String = "",
    var category: String = "",
    override var logo: String = "",
    override var name: String = "",
    override var favorite: Boolean = false,
    override var download: Boolean = false,
    var language: List<Language> = emptyList(),
    @Ignore
    var country: Country = Country(),
    // 此处为一对多关系的关键，把Tv对象插入到数据库时，须将此id设置为相对应国家id
    var countryId: Long = 0,
    var countryName: String = country.name
) : Parcelable, Image {
    override var videoUrl: String
        get() = url
        set(value) {}

    override val transitionName: String
        get() = url
}
