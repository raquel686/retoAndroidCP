package com.project.ecommercecineplanet.domain.usecase

import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.data.repository.CineplanetRepository
import com.project.ecommercecineplanet.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCandyItemsUseCase @Inject constructor(private val repository: CineplanetRepository){
    operator fun invoke(): Flow<Resource<List<CandyItemModel>>> {
        return flow {
            try {
                emit(Resource.Loading())
                val list=repository.getCandyItemsFromApi()
                if (list.isNotEmpty()){
                    emit(Resource.Success(list))
                }else{
                    emit(Resource.Error("Sin datos"))
                }
            }catch (e:Error){
                emit(Resource.Error("Ocurrió un error: ${e.message}"))
            }
        }
    }
}