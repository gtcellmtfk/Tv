package com.bytebyte6.base

import android.content.Context
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Message(
    var id: Int = 0,//字符串资源id
    var actionId: Int = 0,//action字符串资源id
    var message: String = "",//消息
    var args: List<String> = emptyList(),//字符串资源对应参数，如共计：%d or %s
    var longDuration: Boolean = false//时间长短
) : Parcelable {
    fun get(context: Context): String {
        return if (id == 0) message else {
            val args = this.args.toTypedArray()
            context.getString(id, args)
        }
    }
}