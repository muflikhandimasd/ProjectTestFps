package com.muflikhandimasd.projecttest.services

import com.muflikhandimasd.projecttest.entities.EmployeeResponse
import retrofit2.Response
import retrofit2.http.GET

interface EmployeeService {
    @GET("employees.json")
    suspend fun getEmployees(): Response<EmployeeResponse>
}