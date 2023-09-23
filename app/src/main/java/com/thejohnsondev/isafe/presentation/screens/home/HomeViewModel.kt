package com.thejohnsondev.isafe.presentation.screens.home

import com.thejohnsondev.isafe.domain.use_cases.combined.HomeUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCases: HomeUseCases
): BaseViewModel() {

    fun logout() = launch {
        useCases.logout()
    }

}