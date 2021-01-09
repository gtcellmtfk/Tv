package com.bytebyte6.view

import io.reactivex.rxjava3.core.Single
import org.junit.Test
import java.io.FileNotFoundException

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {


    @Test
    fun useAppContext() {
        val res=Result.success("")
//        get("").subscribe({
//
//        }, {
//            it.printStackTrace()
//        })
    }

    fun get(string: String): Single<String> {
        return Single.create {
            if (string.isEmpty()) {
                val fileNotFoundException = FileNotFoundException()
                it.onError(fileNotFoundException)
                return@create
            }
            println("1111111111111111111111111")
            it.onSuccess("success")
        }
    }
}