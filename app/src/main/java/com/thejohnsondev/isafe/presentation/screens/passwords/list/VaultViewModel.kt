package com.thejohnsondev.isafe.presentation.screens.passwords.list

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.AdditionalField
import com.thejohnsondev.isafe.domain.models.BankAccountModel
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordModel
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
        _passwordsList.emit(
            listOf(
                PasswordModel(
                    timestamp = "0",
                    organization = "Google",
                    title ="andrewidobosh361@gmail.com",
                    password = "Pass123$@!",
                    additionalFields = listOf(
                        AdditionalField(
                            "0",
                            "Username",
                            "Andrew"
                        ),
                        AdditionalField(
                            "0",
                            "Temp password",
                            "57494921375"
                        )
                    )
                ),
                PasswordModel(
                    timestamp = "0",
                    organization = "Google",
                    title = "Pass",
                    password = "Pass"
                )
            )
        )
        _bankAccountsList.emit(
            emptyList()
        )
        loaded()
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