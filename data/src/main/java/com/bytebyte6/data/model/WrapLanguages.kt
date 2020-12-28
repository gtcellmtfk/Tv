package com.bytebyte6.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WrapLanguages(
    var language: List<Language> = emptyList()
) : Parcelable {
    override fun toString(): String {
        return if (language.isEmpty()) "[Other]" else language.toString()
    }
}