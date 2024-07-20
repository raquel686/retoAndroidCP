package com.project.ecommercecineplanet.data.network

import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.CandyStoreResponse
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.data.model.PremieresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CineplanetService  @Inject constructor(private val api:ApiClient ){

    suspend fun getPremieres(): PremieresResponse {
        return withContext(Dispatchers.IO){
            val response=api.getPremieres()
            response.body() ?: PremieresResponse(emptyList())
        }
    }

    suspend fun getCandyItems():CandyStoreResponse{
        return withContext(Dispatchers.IO){
            val response=api.getCandyItems()
            response.body() ?: CandyStoreResponse(emptyList())
        }
    }

    suspend fun complete(data: CompleteDto):String{
        return api.complete(data)
    }
}

