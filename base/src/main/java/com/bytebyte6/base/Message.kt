package com.bytebyte6.base

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    val id: Int = 0,
    val actionId: Int = 0,
    val message: String = "",
    val args: List<String> = emptyList(),
    val longDuration: Boolean = false
) : Parcelable {
    fun get(context: Context): String {
        return if (id == 0) message else {
            val args = this.args.toTypedArray()
            context.getString(id, args)
        }
    }
}