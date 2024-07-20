package com.project.ecommercecineplanet.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.ecommercecineplanet.domain.usecase.GetPremieresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val getPremieresUseCase: GetPremieresUseCase
) : ViewModel() {

}