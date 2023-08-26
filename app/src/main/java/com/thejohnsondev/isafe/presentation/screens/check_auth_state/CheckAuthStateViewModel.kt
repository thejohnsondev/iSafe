package com.thejohnsondev.isafe.presentation.screens.check_auth_state

import com.thejohnsondev.isafe.domain.use_cases.auth.GetFirstScreenRoute
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckAuthStateViewModel @Inject constructor(
    private val getFirstScreenRoute: GetFirstScreenRoute
) : BaseViewModel() {

    val firstScreenRoute = MutableStateFlow<String?>(null)

    init {
        fetchFirstScreenRoute()
    }

    private fun fetchFirstScreenRoute() = launch {
        firstScreenRoute.emit(getFirstScreenRoute())
    }

}