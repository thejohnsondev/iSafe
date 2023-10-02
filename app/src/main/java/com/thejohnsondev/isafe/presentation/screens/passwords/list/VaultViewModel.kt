package com.thejohnsondev.isafe.presentation.screens.passwords.list

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.BankAccountModel
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.domain.models.UserPasswordsResponse
import com.thejohnsondev.isafe.domain.use_cases.combined.VaultUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val useCases: VaultUseCases,
    private val dataStore: DataStore
) : BaseViewModel() {

    private val _passwordsList = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _bankAccountsList = MutableStateFlow<List<BankAccountModel>>(emptyList())

    val state = combine(
        _loadingState,
        _passwordsList,
        _bankAccountsList,
        ::mergeSources
    )

    fun perform(action: VaultAction) {
        when (action) {
            is VaultAction.FetchVault -> fetchVault()
        }
    }

    private fun fetchVault() = launchLoading {
        useCases.getAllPasswords(dataStore.getUserData().id.orEmpty()).collect {
            when (it) {
                is UserPasswordsResponse.ResponseFailure -> handleError(it.exception)
                is UserPasswordsResponse.ResponseSuccess -> handlePasswordsList(it.passwords)
            }
        }
        _bankAccountsList.emit(
            emptyList()
        )
        loaded()
    }

    private fun handlePasswordsList(list: List<PasswordModel>) = launch {
        loaded()
        _passwordsList.emit(list)
    }

    private fun mergeSources(
        loadingState: LoadingState,
        passwordsList: List<PasswordModel>,
        bankAccountsList: List<BankAccountModel>
    ): VaultState = VaultState(
        loadingState,
        passwordsList,
        bankAccountsList
    )

}