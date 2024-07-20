package com.project.ecommercecineplanet.data.network

import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.CandyStoreResponse
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.model.CompleteResponse
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.data.model.PremieresResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiClient {
    @GET("candystore")
    suspend fun getCandyItems(): Response<CandyStoreResponse>

    @GET("premieres")
    suspend fun getPremieres(): Response<PremieresResponse>

    @POST("complete")
    suspend fun complete(@Body request: CompleteDto): CompleteResponse



}