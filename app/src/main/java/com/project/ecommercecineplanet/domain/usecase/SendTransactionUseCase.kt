package com.project.ecommercecineplanet.domain.usecase

import com.project.ecommercecineplanet.data.model.PayUResponse
import com.project.ecommercecineplanet.data.model.PayuDto
import com.project.ecommercecineplanet.data.repository.PayuRepository
import com.project.ecommercecineplanet.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SendTransactionUseCase  @Inject constructor(private val repository: PayuRepository){
    operator fun invoke(data:PayuDto):Flow<Resource<PayUResponse>>{
        return flow {
            try {
                emit(Resource.Loading())
                val response=repository.sendTransaction(data)
                if(response.code=="SUCCESS"){
                    emit(Resource.Success(response))
                }else{
                    emit(Resource.Error("Error en la transacción: ${response.error}"))
                }
            }catch (e:Error){
                emit(Resource.Error("Ocurrió un error: ${e.message}"))
            }
        }
    }
}