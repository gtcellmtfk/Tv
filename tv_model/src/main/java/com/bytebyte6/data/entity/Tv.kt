package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
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
    var logo: String = "",
    var name: String = "",
    var favorite: Boolean = false,
    var download: Boolean = false,
    var language: List<Language> = emptyList(),
    @Ignore var country: Country = Country(),
    // 此处为一对多关系的关键，把Tv对象插入到数据库时，须将此id设置为相对应国家id
    var countryId: Long = 0,
    var countryName: String = country.name
) : Parcelable

object TvDiff : DiffUtil.ItemCallback<Tv>() {
    override fun areItemsTheSame(oldItem: Tv, newItem: Tv): Boolean {
        return oldItem.tvId == newItem.tvId
    }

    override fun areContentsTheSame(oldItem: Tv, newItem: Tv): Boolean {
        return oldItem == newItem
    }
}
