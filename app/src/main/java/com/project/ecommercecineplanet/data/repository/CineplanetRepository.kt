package com.project.ecommercecineplanet.data.repository

import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.model.CompleteResponse
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.data.network.CineplanetService
import javax.inject.Inject

class CineplanetRepository@Inject constructor(
    private val api :CineplanetService,

) {
    suspend fun getPremieresFromApi(): List<PremiereModel>{
        val response: List<PremiereModel> = api.getPremieres().premieres
        return response
    }
    suspend fun getCandyItemsFromApi(): List<CandyItemModel>{
        val response: List<CandyItemModel> = api.getCandyItems().items
        return response
    }
    suspend fun complete(data: CompleteDto):CompleteResponse{
        return api.complete(data)
    }
}