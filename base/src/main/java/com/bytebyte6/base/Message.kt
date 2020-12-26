package com.bytebyte6.base

import android.content.Context

data class Message(
    private val id: Int = 0,
     val actionId: Int = 0,
    private val string: String = "",
    private val args: Array<out Any> = emptyArray(),
    val longDuration: Boolean = false
) {
    fun get(context: Context): String {
        return if (id == 0) string else context.getString(id,args)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (id != other.id) return false
        if (string != other.string) return false
        if (!args.contentEquals(other.args)) return false
        if (longDuration != other.longDuration) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + string.hashCode()
        result = 31 * result + args.contentHashCode()
        result = 31 * result + longDuration.hashCode()
        return result
    }
}