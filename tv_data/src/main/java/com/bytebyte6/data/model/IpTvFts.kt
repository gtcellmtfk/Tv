package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Fts4
import kotlinx.android.parcel.Parcelize

@Parcelize
@Fts4(contentEntity = IpTv::class)
@Entity(tableName = "IpTvFts")
data class IpTvFts(
    val url: String,
    var category: String = "",
    val logo: String = "",
    var name: String = "",
    var countryName: String = "",
    var language: String = ""
) : Parcelable {
    companion object {
        const val TAG = "IpTvFts"
        fun toIpTv(fts: IpTvFts): IpTv {
            return IpTv(logo = fts.logo, name = fts.name, url = fts.url)
        }

        fun toIpTvs(ipTvFts: List<IpTvFts>): List<IpTv> {
            val result = mutableListOf<IpTv>()
            for (ipTvFt in ipTvFts) {
                result.add(toIpTv(ipTvFt))
            }
            return result
        }
    }
}