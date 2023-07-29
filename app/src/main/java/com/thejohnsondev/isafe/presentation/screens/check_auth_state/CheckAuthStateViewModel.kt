package com.thejohnsondev.isafe.presentation.screens.check_auth_state

import androidx.lifecycle.ViewModel
import com.thejohnsondev.isafe.domain.use_cases.auth.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckAuthStateViewModel @Inject constructor(
    private val isUserLoggedIn: IsUserLoggedInUseCase
): ViewModel() {

    fun getIsUserLoggedIn(): Boolean = isUserLoggedIn()

}