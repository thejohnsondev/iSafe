package com.thejohnsondev.presentation.check_auth_state

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.domain.GetFirstScreenRouteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckAuthStateViewModel @Inject constructor(
    private val getFirstScreenRoute: GetFirstScreenRouteUseCase
) : BaseViewModel() {

    val firstScreenRoute = MutableStateFlow<String?>(null)

    init {
        fetchFirstScreenRoute()
    }

    private fun fetchFirstScreenRoute() = launch {
        firstScreenRoute.emit(getFirstScreenRoute())
    }

}