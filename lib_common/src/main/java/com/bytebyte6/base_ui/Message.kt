package com.bytebyte6.base_ui

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: Int = 0,
    var actionId: Int = 0,
    var message: String = "",
    var args: List<String> = emptyList(),
    var longDuration: Boolean = false
) : Parcelable {
    fun get(context: Context): String {
        return if (id == 0) message else {
            val args = this.args.toTypedArray()
            context.getString(id, args)
        }
    }
}