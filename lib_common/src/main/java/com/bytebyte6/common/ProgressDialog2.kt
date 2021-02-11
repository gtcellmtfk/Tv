package com.bytebyte6.common

import android.app.ProgressDialog
import android.content.Context

class ProgressDialog2(context: Context) : ProgressDialog(context) {
    fun setMessage2(message: CharSequence?) {
        setMessage(message)
    }
}