package com.android.starter.network

import android.util.Log
import com.android.starter.model.Program
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class Repository {

    private val service = Retrofit.Builder()
        .baseUrl("http://192.168.1.2:3000/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
        .create(Api::class.java)

    suspend fun started(): Program {
        Log.i("REQUEST", "WHATNOW")
        val call = service.started()
        return call.body()?.let { response ->
            Program.values().first { it.programString == response }
        } ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun logData(): String {
        Log.i("REQUEST", "LOGDATA")
        val call = service.logData()
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun done(): String {
        Log.i("REQUEST", "DONE")
        val call = service.done()
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }

    suspend fun test(): String {
        val call = service.test()
        return call.body() ?: throw ApiError(call.errorBody()!!.toString())
    }
}

class ApiError(body: String): Exception(body)