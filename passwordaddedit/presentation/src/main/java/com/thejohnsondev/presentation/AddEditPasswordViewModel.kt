package com.thejohnsondev.presentation

import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.common.combine
import com.thejohnsondev.common.encryptModel
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.AddEditPasswordUseCases
import com.thejohnsondev.model.AdditionalField
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.PasswordModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class AddEditPasswordViewModel @Inject constructor(
    private val useCases: AddEditPasswordUseCases,
    private val dataStore: DataStore
) : BaseViewModel() {

    private val _organization = MutableStateFlow(EMPTY)
    private val _title = MutableStateFlow(EMPTY)
    private val _password = MutableStateFlow(EMPTY)
    private val _passwordId = MutableStateFlow<String?>(null)
    private val _additionalFields = MutableStateFlow<List<AdditionalField>>(emptyList())
    private val _isEdit = MutableStateFlow(false)

    val state = combine(
        _loadingState,
        _organization,
        _title,
        _password,
        _additionalFields,
        _isEdit,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.AddAdditionalField -> addAdditionalField(action.timeStamp)
            is Action.EnterAdditionalFieldTitle -> enterAdditionalFieldTitle(
                action.timeStamp,
                action.title
            )

            is Action.EnterAdditionalFieldValue -> enterAdditionalFieldValue(
                action.timeStamp,
                action.value
            )

            is Action.EnterOrganization -> enterOrganization(action.organization)
            is Action.EnterPassword -> enterPassword(action.password)
            is Action.EnterTitle -> enterTitle(action.title)
            is Action.SavePassword -> savePassword()
            is Action.SetPasswordModelForEdit -> setPasswordModelForEdit(action.passwordModel)
            is Action.RemoveAdditionalField -> removeAdditionalField(action.timeStamp)
        }
    }

    private fun removeAdditionalField(fieldTimeStamp: String) = launch {
        val currentList = _additionalFields.value.toMutableList()
        currentList.removeIf { it.id == fieldTimeStamp }
        _additionalFields.emit(currentList)
    }

    private fun setPasswordModelForEdit(passwordModel: PasswordModel) = launch {
        _organization.emit(passwordModel.organization)
        _title.emit(passwordModel.title)
        _password.emit(passwordModel.password)
        _additionalFields.emit(passwordModel.additionalFields)
        _passwordId.emit(passwordModel.id)
        _isEdit.emit(true)
    }

    private fun enterOrganization(organization: String) = launch {
        _organization.emit(organization)
    }

    private fun enterTitle(title: String) = launch {
        _title.emit(title)
    }

    private fun enterPassword(password: String) = launch {
        _password.emit(password)
    }

    private fun addAdditionalField(timeStamp: String) = launch {
        val currentList = _additionalFields.value.toMutableList()
        currentList.add(
            AdditionalField(
                id = timeStamp,
                title = EMPTY,
                value = EMPTY
            )
        )
        _additionalFields.emit(currentList)
    }

    private fun enterAdditionalFieldTitle(timeStamp: String, title: String) = launch {
        val itemIndex = _additionalFields.value.indexOf(
            _additionalFields.value.find {
                it.id == timeStamp
            }
        )
        if (itemIndex == -1) return@launch
        val field = AdditionalField(
            id = timeStamp,
            title = title,
            value = _additionalFields.value[itemIndex].value
        )
        val newList = _additionalFields.value.toMutableList()
        newList.removeAt(itemIndex)
        newList.add(itemIndex, field)
        _additionalFields.emit(newList)
    }

    private fun enterAdditionalFieldValue(timeStamp: String, value: String) = launch {
        val itemIndex = _additionalFields.value.indexOf(
            _additionalFields.value.find {
                it.id == timeStamp
            }
        )
        if (itemIndex == -1) return@launch
        val field = AdditionalField(
            id = timeStamp,
            title = _additionalFields.value[itemIndex].title,
            value = value
        )
        val newList = _additionalFields.value.toMutableList()
        newList.removeAt(itemIndex)
        newList.add(itemIndex, field)
        _additionalFields.emit(newList)
    }

    private fun savePassword() = launch {
        val passwordModel = PasswordModel(
            id = _passwordId.value,
            _organization.value,
            null,
            _title.value,
            _password.value,
            System.currentTimeMillis().toString(),
            _additionalFields.value.onEach { it.id = null }
        )
        val encryptedPasswordModel = passwordModel.encryptModel(dataStore.getUserKey())
        if (_isEdit.value) {
            useCases.updatePassword(encryptedPasswordModel).first().fold(
                ifLeft = { handleError(it) },
                ifRight = { handlePasswordSaved() }
            )
        } else {
            useCases.createPassword(encryptedPasswordModel).first().fold(
                ifLeft = { handleError(it) },
                ifRight = { handlePasswordSaved() }
            )
        }
    }

    private fun handlePasswordSaved() = launch {
        val infoText = if (_isEdit.value) "Password edited" else "Password added"
        sendEvent(OneTimeEvent.SuccessNavigation(infoText))
    }


    private fun mergeSources(
        loadingState: LoadingState,
        organization: String,
        title: String,
        password: String,
        additionalFields: List<AdditionalField>,
        isEdit: Boolean
    ): State = State(
        loadingState = loadingState,
        organization = organization,
        title = title,
        password = password,
        additionalFields = additionalFields,
        isEdit = isEdit
    )

    sealed class Action {
        class EnterOrganization(val organization: String) : Action()
        class EnterTitle(val title: String) : Action()
        class EnterPassword(val password: String) : Action()
        class AddAdditionalField(val timeStamp: String) : Action()
        class EnterAdditionalFieldTitle(val timeStamp: String, val title: String) : Action()
        class EnterAdditionalFieldValue(val timeStamp: String, val value: String) : Action()
        class RemoveAdditionalField(val timeStamp: String) : Action()
        object SavePassword : Action()
        class SetPasswordModelForEdit(val passwordModel: PasswordModel) : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val organization: String = EMPTY,
        val title: String = EMPTY,
        val password: String = EMPTY,
        val additionalFields: List<AdditionalField> = emptyList(),
        val timeStamp: String = EMPTY,
        val isEdit: Boolean = false
    )

}