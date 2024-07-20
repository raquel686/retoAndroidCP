package com.project.ecommercecineplanet.data.network

import com.project.ecommercecineplanet.data.model.PayUResponse
import com.project.ecommercecineplanet.data.model.PayuDto
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PayuClient {

    @Headers("Content-Type: application/json","Accept: application/json")
    @POST("payments-api/4.0/service.cgi")
    suspend fun submitTransaction(@Body request: PayuDto): PayUResponse
}