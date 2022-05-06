package com.android.starter.network

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("/what_now")
    suspend fun started(): Response<String>

    @GET("/logdata")
    suspend fun logData(): Response<String>

    @GET("/done")
    suspend fun done(): Response<String>

    @GET("/")
    suspend fun test(): Response<String>
}