package com.muflikhandimasd.projecttest.retrofit

import com.muflikhandimasd.projecttest.services.EmployeeService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://s3.amazonaws.com/sq-mobile-interview/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: EmployeeService by lazy {
        retrofit.create(EmployeeService::class.java)
    }
}