package com.bytebyte6.data

import com.bytebyte6.data.entity.Tv
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface TvApi {
    @GET("channels.json")
    fun getTvs(): Single<List<Tv>>
}