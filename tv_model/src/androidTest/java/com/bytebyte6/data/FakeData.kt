package com.bytebyte6.data

import com.bytebyte6.data.entity.Tv

const val FAKE_SIZE = 200

val tvList = mutableListOf<Tv>().apply {
    for (i in 0 until FAKE_SIZE) {
        add(
            Tv(
                url = "https://y5w8j4a${i}9.ssl.hwcdn.net/andprivehd/tracks-v1a1/a.m3u8",
                name = "A$i"
            )
        )
    }
}
