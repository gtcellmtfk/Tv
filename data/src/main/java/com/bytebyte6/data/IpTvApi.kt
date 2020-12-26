package com.bytebyte6.data

import com.bytebyte6.data.model.IpTv
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface IpTvApi {
    @GET("channels.json")
    fun getList(): Single<List<IpTv>>
}