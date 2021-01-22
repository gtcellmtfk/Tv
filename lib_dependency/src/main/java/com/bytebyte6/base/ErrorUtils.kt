package com.bytebyte6.base

import com.google.gson.JsonParseException
import org.json.JSONException
import retrofit2.HttpException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.text.ParseException
import javax.net.ssl.SSLHandshakeException

object ErrorUtils {
    fun getMessage(e: Throwable?): Int {
        return if (e is HttpException) {
            R.string.network_error
        } else if (e is NumberFormatException ||
            e is JsonParseException ||
            e is JSONException ||
            e is ParseException
        ) {
            R.string.data_parse_error
        } else if (e is SocketException) {
            R.string.network_connect_error
        } else if (e is UnknownHostException || e is SSLHandshakeException) {
            R.string.network_un_connected
        } else if (e is SocketTimeoutException) {
            R.string.network_timeout
        } else {
            R.string.unkown_error
        }
    }
}