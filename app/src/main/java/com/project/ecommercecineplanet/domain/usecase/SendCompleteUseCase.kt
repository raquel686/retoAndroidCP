package com.project.ecommercecineplanet.domain.usecase

import android.util.Log
import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.repository.CineplanetRepository
import com.project.ecommercecineplanet.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendCompleteUseCase @Inject constructor(private val repository: CineplanetRepository){
    operator fun invoke(data:CompleteDto): Flow<Resource<String>> {
        return flow {
            try {
                emit(Resource.Loading())
                val response=repository.complete(data)
                Log.i("complete view",response.toString())
                if (response.isBlank()){
                    emit(Resource.Error("Sin respuesta"))
                }else{
                    emit(Resource.Success(response))
                }
            }catch (e:Error){
                emit(Resource.Error("Ocurrió un error: ${e.message}"))
            }
        }
    }
}