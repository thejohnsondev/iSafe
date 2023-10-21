package com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.AdditionalField
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.domain.use_cases.combined.AddEditPasswordUseCases
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import com.thejohnsondev.isafe.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class AddEditPasswordViewModel @Inject constructor(
    private val useCases: AddEditPasswordUseCases,
    private val dataStore: DataStore
) : BaseViewModel() {

    private val _organization = MutableStateFlow(EMPTY)
    private val _title = MutableStateFlow(EMPTY)
    private val _password = MutableStateFlow(EMPTY)
    private val _timeStamp = MutableStateFlow(EMPTY)
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
        _timeStamp.emit(passwordModel.id)
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

    private fun savePassword() = launchLoading {
        val timeStamp =
            if (_isEdit.value) _timeStamp.value else System.currentTimeMillis().toString()
        val passwordModel = PasswordModel(
            id = timeStamp,
            _organization.value,
            null,
            _title.value,
            _password.value,
            _additionalFields.value
        )
        if (_isEdit.value) {
            useCases.updatePassword(dataStore.getUserData().id.orEmpty(), passwordModel).collect {
                when (it) {
                    is DatabaseResponse.ResponseFailure -> handleError(it.exception)
                    is DatabaseResponse.ResponseSuccess -> handlePasswordSaved()
                }
            }
        } else {
            useCases.createPassword(dataStore.getUserData().id.orEmpty(), passwordModel).collect {
                when (it) {
                    is DatabaseResponse.ResponseFailure -> handleError(it.exception)
                    is DatabaseResponse.ResponseSuccess -> handlePasswordSaved()
                }
            }
        }
    }

    private fun handlePasswordSaved() = launch {
        val infoText = if (_isEdit.value) "Password edited" else "Password added"
        sendEvent(OneTimeEvent.InfoToast(infoText))
        sendEvent(OneTimeEvent.SuccessNavigation)
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