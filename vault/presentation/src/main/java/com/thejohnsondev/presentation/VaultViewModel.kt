package com.thejohnsondev.presentation


import com.thejohnsondev.common.key_utils.KeyUtils
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.common.combine
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.model.BankAccountModel
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.model.settings.SettingsConfig
import com.thejohnsondev.presentation.usecases.VaultUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class VaultViewModel @Inject constructor(
    private val useCases: VaultUseCases,
    private val dataStore: DataStore,
    private val keyUtils: KeyUtils
) : BaseViewModel() {

    private val _allPasswordsList = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _allBankAccountsList = MutableStateFlow<List<BankAccountModel>>(emptyList())
    private val _passwordsListFetched = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _passwordsList = MutableStateFlow<List<PasswordModel>>(emptyList())
    private val _bankAccountsList = MutableStateFlow<List<BankAccountModel>>(emptyList())
    private val _isSearching = MutableStateFlow(false)
    private val _isReordering = MutableStateFlow(false)
    private val _settingsConfig = useCases.getSettingsConfig.invoke()

    val state = combine(
        _loadingState,
        _passwordsList,
        _bankAccountsList,
        _isSearching,
        _isReordering,
        _settingsConfig,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.FetchVault -> fetchVault()
            is Action.DeletePassword -> deletePassword(action.password)
            is Action.Search -> search(action.query, action.isDeepSearchEnabled)
            is Action.StopSearching -> stopSearching()
            is Action.ToggleReordering -> toggleReordering()
            is Action.Reorder -> reorder(action.from, action.to)
            is Action.SaveNewOrderedList -> saveNewOrderedList()
        }
    }

    private fun saveNewOrderedList() = launch {
        useCases.updatePasswordsUseCase(_passwordsList.value)
            .collect {
                when (it) {
                    is DatabaseResponse.ResponseFailure -> handleError(it.exception)
                    is DatabaseResponse.ResponseSuccess -> handleUpdatePasswordListSuccess()
                }
            }

    }

    private fun handleUpdatePasswordListSuccess() = launchLoading {
        _passwordsListFetched.emit(_passwordsList.value)
        _isReordering.emit(false)
        loaded()
    }

    private fun toggleReordering() = launch {
        _isReordering.emit(!_isReordering.value)
        if (!_isReordering.value) {
            _passwordsList.emit(_passwordsListFetched.value)
        }
    }

    private fun reorder(from: Int, to: Int) = launch {
        val newList = _passwordsList.value.toMutableList().apply {
            add(to, removeAt(from))
        }
        handlePasswordsList(newList)
    }

    private fun stopSearching() = launch {
        _isSearching.emit(false)
        _passwordsList.emit(_allPasswordsList.value)
        _bankAccountsList.emit(_allBankAccountsList.value)
    }

    private fun search(query: String, isDeepSearchEnabled: Boolean) = launch {
        _isSearching.emit(true)
        searchPasswords(query, isDeepSearchEnabled)
        searchBankAccounts(query, isDeepSearchEnabled)
    }

    private suspend fun searchPasswords(query: String, isDeepSearchEnabled: Boolean) {
        if (query.isBlank()) {
            _passwordsList.emit(_allPasswordsList.value)
            _isSearching.emit(false)
            return
        }
        val filteredPasswordList = if (isDeepSearchEnabled) {
            _allPasswordsList.value.filter {
                it.title.lowercase().contains(query.lowercase())
                        || it.organization.lowercase().contains(query.lowercase())
                        || it.additionalFields.any { field ->
                    field.value.lowercase().contains(query.lowercase())
                            || field.title.lowercase().contains(query.lowercase())
                }
            }
        } else {
            _allPasswordsList.value.filter {
                it.title.lowercase().contains(query.lowercase())
            }
        }
        _passwordsList.emit(filteredPasswordList)
    }

    private suspend fun searchBankAccounts(query: String, isDeepSearchEnabled: Boolean) {
        if (query.isBlank()) {
            _bankAccountsList.emit(_allBankAccountsList.value)
            _isSearching.emit(false)
            return
        }
        val filteredBankAccountsList = _allBankAccountsList.value.filter {
            it.userName.lowercase().contains(query.lowercase()) or it.cardNumber.lowercase()
                .contains(query.lowercase())
        }
        _bankAccountsList.emit(filteredBankAccountsList)
    }

    private fun deletePassword(passwordModel: PasswordModel) = launch {
        passwordModel.id?.let {
            useCases.deletePassword(it).first().fold(
                ifLeft = ::handleError,
                ifRight = {
                    deletePasswordFromList(passwordModel)
                }
            )
        }
    }

    private fun deletePasswordFromList(passwordModel: PasswordModel) = launch {
        val passwordsList = _passwordsList.value.toMutableList()
        passwordsList.removeIf { it.id == passwordModel.id }
        _passwordsList.emit(passwordsList)
        _allPasswordsList.emit(passwordsList)
    }

    private fun fetchVault() = launchLoading {
        useCases.getAllPasswords().collect {
            it.fold(
                ifLeft = ::handleError,
                ifRight = {
                    val decryptedPasswordList = it.map {
                        keyUtils.decryptPasswordModel(it, dataStore.getUserKey())
                    }.sortedByDescending { it.lastEdit }
                    handlePasswordsList(decryptedPasswordList)
                    _passwordsListFetched.emit(decryptedPasswordList)
                    loaded()
                }

            )
        }
        _bankAccountsList.emit(
            emptyList()
        )
        _allBankAccountsList.emit(
            emptyList()
        )
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
        isSearching: Boolean,
        isReordering: Boolean,
        settingsConfig: SettingsConfig
    ): State = State(
        loadingState,
        passwordsList,
        bankAccountsList,
        isSearching,
        isReordering,
        settingsConfig.generalSettings.isDeepSearchEnabled
    )

    sealed class Action {
        object FetchVault : Action()
        class DeletePassword(val password: PasswordModel) : Action()
        class Search(val query: String, val isDeepSearchEnabled: Boolean) : Action()
        object ToggleReordering: Action()
        object StopSearching : Action()
        class Reorder(val from: Int, val to: Int): Action()
        object SaveNewOrderedList: Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val passwordsList: List<PasswordModel> = emptyList(),
        val bankAccountsList: List<BankAccountModel> = emptyList(),
        val isSearching: Boolean = false,
        val isReordering: Boolean = false,
        val isDeepSearchEnabled: Boolean = false
    )

}