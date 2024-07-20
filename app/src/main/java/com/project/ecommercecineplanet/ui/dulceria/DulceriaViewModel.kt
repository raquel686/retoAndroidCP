package com.project.ecommercecineplanet.ui.dulceria

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ecommercecineplanet.data.model.CandyItemModel
import com.project.ecommercecineplanet.data.model.CompleteDto
import com.project.ecommercecineplanet.data.model.PayuDto
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.data.model.toModel
import com.project.ecommercecineplanet.domain.model.CandyItemDomainModel
import com.project.ecommercecineplanet.domain.model.PayuDtoDomainModel
import com.project.ecommercecineplanet.domain.model.TransactionDomainModel
import com.project.ecommercecineplanet.domain.usecase.GetCandyItemsUseCase
import com.project.ecommercecineplanet.domain.usecase.GetPremieresUseCase
import com.project.ecommercecineplanet.domain.usecase.SendCompleteUseCase
import com.project.ecommercecineplanet.domain.usecase.SendTransactionUseCase
import com.project.ecommercecineplanet.ui.home.HomeViewModel
import com.project.ecommercecineplanet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.IllegalFormatConversionException
import javax.inject.Inject

@HiltViewModel
class DulceriaViewModel  @Inject constructor(
    private val getCandyItemsUseCase: GetCandyItemsUseCase,
    private val sendTransactionUseCase: SendTransactionUseCase,
    private val sendCompleteUseCase: SendCompleteUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> get() = _uiState

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent: SharedFlow<UIEvent> get() = _uiEvent

    private val _total= MutableStateFlow<String>("0.00")
    val total:StateFlow<String> get() = _total


    fun enviarTransacecion(data:TransactionDomainModel){
        viewModelScope.launch {
            sendTransactionUseCase(PayuDtoDomainModel(data).toModel()).onEach {res->
                when(res){
                    is Resource.Error ->{
                        _uiState.value = UIState(errorMessage = "Error: ${res.message}")
                        Log.i("transac view",res.message.toString())
                    }
                    is Resource.Loading ->  _uiState.value = UIState(isLoading = true)
                    is Resource.Success -> {
                        _uiEvent.emit(UIEvent.TransaccionExitosa(res.data!!.transactionResponse.operationDate!!))
                    }
                }

            }.launchIn(viewModelScope)
        }
    }

    fun enviarConfirmacion(data:CompleteDto){
        viewModelScope.launch {
            sendCompleteUseCase(data).onEach { res->
                when(res){
                    is Resource.Error ->  _uiState.value = UIState(errorMessage = "Error: $res")
                    is Resource.Loading ->  _uiState.value = UIState(isLoading = true)
                    is Resource.Success -> {
                        _uiEvent.emit(UIEvent.ConfirmacionExitosa)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
    fun obtenerCandiesPremieres(){
        viewModelScope.launch {
            getCandyItemsUseCase().onEach {res->
                when(res){
                    is Resource.Error -> {
                        _uiState.value = UIState(errorMessage = "Error: ${res.message}")
                    }
                    is Resource.Loading -> {
                        _uiState.value = UIState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _uiState.value = UIState(dataList1 = res.data!!)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun actualizarTotal(list:List<CandyItemDomainModel>){
        viewModelScope.launch {
            val total = list.sumOf {
                (it.price?.toDoubleOrNull() ?: 0.0) * (it.cantidad ?: 0)
            }
            Log.i("total viewmodel 2",total.toString())
            val formattedTotal = try {
                String.format("%.2f", total)
            } catch (e: IllegalFormatConversionException) {
                Log.e("Formatting Error", "Error formatting total: ${e.message}")
                total.toString()
            }

            _total.value= formattedTotal
            _uiEvent.emit(UIEvent.ChangeTotal)
        }
    }


    data class UIState(
        val isLoading: Boolean = false,
        val dataList1: List<CandyItemModel> = emptyList(),
        val errorMessage: String? = null
    )

    sealed class UIEvent {
        data class ShowError(val message: String) : UIEvent()
        object ChangeTotal:UIEvent()
        data class TransaccionExitosa(val operation: String):UIEvent()
        object ConfirmacionExitosa:UIEvent()
        object ShowLoading : UIEvent()
        object HideLoading : UIEvent()
        object NavigateToLogin : UIEvent()
    }
}