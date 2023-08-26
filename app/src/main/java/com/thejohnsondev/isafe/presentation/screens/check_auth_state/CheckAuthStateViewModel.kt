package com.thejohnsondev.isafe.presentation.screens.check_auth_state

import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCase
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckAuthStateViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedInUseCase
) : BaseViewModel() {

    val isUserLoggedInState = MutableStateFlow<Boolean?>(null)

    init {
        fetchIsUserLoggedIn()
    }

    private fun fetchIsUserLoggedIn() = launch {
        isUserLoggedInState.emit(isUserLoggedIn())
    }

}