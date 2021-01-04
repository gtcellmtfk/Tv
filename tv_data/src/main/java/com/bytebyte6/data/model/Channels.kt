package com.bytebyte6.data.model

import android.os.Parcelable
import com.bytebyte6.data.entity.Tv
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Channels(
    var list: List<Tv> = emptyList()
) : Parcelable