package com.project.ecommercecineplanet.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.ecommercecineplanet.data.model.PremiereModel
import com.project.ecommercecineplanet.domain.usecase.GetPremieresUseCase
import com.project.ecommercecineplanet.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel  @Inject constructor(private val getPremieresUseCase: GetPremieresUseCase
): ViewModel() {

    private val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState> get() = _uiState

    private val _uiEvent = MutableSharedFlow<UIEvent>()
    val uiEvent: SharedFlow<UIEvent> get() = _uiEvent

    init {
        _uiState.value = UIState()
    }

    fun obtenerListaPremieres(){
        viewModelScope.launch {
            getPremieresUseCase().onEach {res->
                when(res){
                    is Resource.Error -> {
                        _uiState.value = UIState(errorMessage = "Error: $res")
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
    fun onItemSelected() {
        viewModelScope.launch {
            _uiEvent.emit(UIEvent.NavigateToLogin)
        }
    }

    data class UIState(
        val isLoading: Boolean = false,
        val dataList1: List<PremiereModel> = emptyList(),
        val errorMessage: String? = null
    )

    sealed class UIEvent {
        data class ShowError(val message: String) : UIEvent()
        object ShowLoading : UIEvent()
        object HideLoading : UIEvent()
        object NavigateToLogin : UIEvent()
    }
}