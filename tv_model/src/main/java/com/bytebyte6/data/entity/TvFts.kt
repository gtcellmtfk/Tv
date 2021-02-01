package com.bytebyte6.data.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.android.parcel.Parcelize

@Parcelize
@Fts4(contentEntity = Tv::class)
@Entity
@Keep
data class TvFts(
    val tvId: Long,
    var url: String,
    var category: String = "",
    var logo: String = "",
    var name: String = "",
    var favorite: Boolean = false,
    var countryName: String = "",
    var language: String = ""
) : Parcelable {
    companion object {
        const val TAG = "TvFts"
         fun toTv(fts: TvFts): Tv {
            return Tv(
                logo = fts.logo,
                name = fts.name,
                url = fts.url,
                tvId = fts.tvId,
                favorite = fts.favorite
            )
        }

        fun toTvs(tvFts: List<TvFts>): List<Tv> {
            val result = mutableListOf<Tv>()
            for (ipTvFt in tvFts) {
                result.add(
                    toTv(
                        ipTvFt
                    )
                )
            }
            return result
        }
    }
}