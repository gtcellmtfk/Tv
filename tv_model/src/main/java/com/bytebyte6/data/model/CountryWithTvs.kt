package com.bytebyte6.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Embedded
import androidx.room.Relation
import com.bytebyte6.data.entity.Country
import com.bytebyte6.data.entity.Tv
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
data class CountryWithTvs(
    @Embedded val country: Country,
    @Relation(
        parentColumn = "countryId",
        entityColumn = "countryId"
    )
    val tvs: List<Tv> = emptyList()
) : Parcelable