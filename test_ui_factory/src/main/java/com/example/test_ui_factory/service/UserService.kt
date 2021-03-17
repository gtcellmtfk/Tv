package com.example.test_ui_factory.service

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @GET
    fun query()

    @POST
    fun update()

    @DELETE
    fun delete()

    @POST
    fun insert()
}