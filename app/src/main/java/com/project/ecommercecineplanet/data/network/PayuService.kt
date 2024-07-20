package com.project.ecommercecineplanet.data.network

import com.project.ecommercecineplanet.data.model.PayUResponse
import com.project.ecommercecineplanet.data.model.PayuDto
import javax.inject.Inject

class PayuService @Inject constructor(private val api:PayuClient ) {
    suspend fun transaction(data:PayuDto):PayUResponse{
        return api.submitTransaction(data)
    }
}