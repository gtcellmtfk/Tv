package com.bytebyte6.base;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import retrofit2.HttpException;

public class ErrorUtils {
    public static Integer getMessage(Throwable e) {
        int message;
        if (e instanceof HttpException) {
            message = (R.string.network_error);
        } else if (e instanceof NumberFormatException ||
                e instanceof JsonParseException ||
                e instanceof JSONException ||
                e instanceof ParseException) {
            message = (R.string.data_parse_error);
        } else if (e instanceof SocketException) {
            message = (R.string.network_connect_error);
        } else if (e instanceof UnknownHostException) {
            message = (R.string.network_un_connected);
        } else if (e instanceof SocketTimeoutException) {
            message = (R.string.network_timeout);
        } else {
            message = (R.string.unkown_error);
        }
        return message;
    }
}