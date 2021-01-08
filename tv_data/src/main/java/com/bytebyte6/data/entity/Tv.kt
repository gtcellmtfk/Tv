package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.bytebyte6.data.model.Language
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [
        Index(value = ["tvId"], unique = true),
        Index(value = ["url"], unique = true)
    ]
)
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

) : Parcelable {
    companion object {
        const val TAG = "Tv"
    }
}

object TvDiff : DiffUtil.ItemCallback<Tv>() {
    override fun areItemsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem.url == oldItem.url

    override fun areContentsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem == oldItem
}