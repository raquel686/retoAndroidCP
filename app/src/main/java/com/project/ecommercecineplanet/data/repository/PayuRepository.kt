package com.project.ecommercecineplanet.data.repository


import com.project.ecommercecineplanet.data.model.PayUResponse
import com.project.ecommercecineplanet.data.model.PayuDto
import com.project.ecommercecineplanet.data.network.PayuService
import javax.inject.Inject

class PayuRepository @Inject constructor(
    private val api : PayuService,
    )  {
    suspend fun sendTransaction(data:PayuDto):PayUResponse{
        return api.transaction(data)
    }
}