package com.thejohnsondev.isafe.presentation.screens.passwords.list

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.BankAccountModel
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
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

    private val _allPasswordsList = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _allBankAccountsList = MutableStateFlow<List<BankAccountModel>>(emptyList())
    private val _passwordsList = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _bankAccountsList = MutableStateFlow<List<BankAccountModel>>(emptyList())
    private val _isSearching = MutableStateFlow(false)

    val state = combine(
        _loadingState,
        _passwordsList,
        _bankAccountsList,
        _isSearching,
        ::mergeSources
    )

    fun perform(action: VaultAction) {
        when (action) {
            is VaultAction.FetchVault -> fetchVault()
            is VaultAction.DeletePassword -> deletePassword(action.password)
            is VaultAction.Search -> search(action.query)
            VaultAction.StopSearching -> stopSearching()
        }
    }

    private fun stopSearching() = launch {
        _isSearching.emit(false)
        _passwordsList.emit(_allPasswordsList.value)
        _bankAccountsList.emit(_allBankAccountsList.value)
    }

    private fun search(query: String) = launch {
        _isSearching.emit(true)
        searchPasswords(query)
        searchBankAccounts(query)
    }

    private suspend fun searchPasswords(query: String) {
        if (query.isBlank()) {
            _passwordsList.emit(_allPasswordsList.value)
            _isSearching.emit(false)
            return
        }
        val filteredPasswordList = _allPasswordsList.value.filter {
            it.title.contains(query) or it.organization.contains(query)
        }
        _passwordsList.emit(filteredPasswordList)
    }

    private suspend fun searchBankAccounts(query: String) {
        if (query.isBlank()) {
            _bankAccountsList.emit(_allBankAccountsList.value)
            _isSearching.emit(false)
            return
        }
        val filteredBankAccountsList = _allBankAccountsList.value.filter {
            it.userName.contains(query) or it.cardNumber.contains(query)
        }
        _bankAccountsList.emit(filteredBankAccountsList)
    }

    private fun deletePassword(passwordModel: PasswordModel) = launch {
        useCases.deletePassword(dataStore.getUserData().id.orEmpty(), passwordModel.timestamp).collect {
            when (it) {
                is DatabaseResponse.ResponseFailure -> handleError(it.exception)
                is DatabaseResponse.ResponseSuccess -> deletePasswordFromList(passwordModel)
            }
        }
    }

    private fun deletePasswordFromList(passwordModel: PasswordModel) = launch {
        val passwordsList = _passwordsList.value.toMutableList()
        passwordsList.removeIf { it.timestamp == passwordModel.timestamp }
        _passwordsList.emit(passwordsList)
        _allPasswordsList.emit(passwordsList)
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
        _allBankAccountsList.emit(
            emptyList()
        )
        loaded()
    }

    private fun handlePasswordsList(list: List<PasswordModel>) = launch {
        loaded()
        _passwordsList.emit(list)
        _allPasswordsList.emit(list)
    }

    private fun mergeSources(
        loadingState: LoadingState,
        passwordsList: List<PasswordModel>,
        bankAccountsList: List<BankAccountModel>,
        isSearching: Boolean
    ): VaultState = VaultState(
        loadingState,
        passwordsList,
        bankAccountsList,
        isSearching
    )

}