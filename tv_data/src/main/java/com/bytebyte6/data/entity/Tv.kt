package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.*
import com.bytebyte6.data.model.Country
import com.bytebyte6.data.model.Language
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    indices = [
        Index(value = ["tvId"])
    ]
)
data class Tv(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tvId")
    val tvId: Long = 0,
    var url: String,
    var category: String = "Other",
    var logo: String = "",
    var name: String = "",
    var language: List<Language> = emptyList(),
    @Embedded var country: Country = Country()
) : Parcelable {
    companion object {
        const val TAG = "Tv"
        fun init(list: List<Tv>): List<Tv> {
            return list.apply {
                map {
                    if (it.category.isEmpty()) {
                        it.category = "Other"
                    }
                    if (it.language.isEmpty()) {
                        it.language = mutableListOf(Language("Other"))
                    }
                    if (it.country.countryName.isEmpty()) {
                        it.country.countryName = "Other"
                    }
                }
            }
        }
    }

}

object TvDiff : DiffUtil.ItemCallback<Tv>() {
    override fun areItemsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem.url == oldItem.url

    override fun areContentsTheSame(oldItem: Tv, newItem: Tv): Boolean = oldItem == oldItem
}