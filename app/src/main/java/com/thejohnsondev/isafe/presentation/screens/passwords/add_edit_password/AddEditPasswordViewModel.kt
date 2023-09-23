package com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password

import com.thejohnsondev.isafe.domain.models.AdditionalField
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.utils.EMPTY
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AddEditPasswordViewModel @Inject constructor(

) : BaseViewModel() {

    private val _organization = MutableStateFlow(EMPTY)
    private val _title = MutableStateFlow(EMPTY)
    private val _password = MutableStateFlow(EMPTY)
    private val _additionalFields = MutableStateFlow<List<AdditionalField>>(emptyList())

    val state = combine(
        _loadingState,
        _organization,
        _title,
        _password,
        _additionalFields,
        ::mergeSources
    )

    fun perform(action: AddEditPasswordAction) {
        when (action) {
            is AddEditPasswordAction.AddAdditionalField -> addAdditionalField(action.timeStamp)
            is AddEditPasswordAction.EnterAdditionalFieldTitle -> enterAdditionalFieldTitle(
                action.timeStamp,
                action.title
            )

            is AddEditPasswordAction.EnterAdditionalFieldValue -> enterAdditionalFieldValue(
                action.timeStamp,
                action.value
            )

            is AddEditPasswordAction.EnterOrganization -> enterOrganization(action.organization)
            is AddEditPasswordAction.EnterPassword -> enterPassword(action.password)
            is AddEditPasswordAction.EnterTitle -> enterTitle(action.title)
            is AddEditPasswordAction.SavePassword -> savePassword()
        }
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
                timeStamp = timeStamp,
                title = EMPTY,
                value = EMPTY
            )
        )
        _additionalFields.emit(currentList)
    }

    private fun enterAdditionalFieldTitle(timeStamp: String, title: String) = launch {
        val itemIndex = _additionalFields.value.indexOf(
            _additionalFields.value.find {
                it.timeStamp == timeStamp
            }
        )
        if (itemIndex == -1) return@launch
        val field = AdditionalField(
            timeStamp = timeStamp,
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
                it.timeStamp == timeStamp
            }
        )
        if (itemIndex == -1) return@launch
        val field = AdditionalField(
            timeStamp = timeStamp,
            title = _additionalFields.value[itemIndex].title,
            value = value
        )
        val newList = _additionalFields.value.toMutableList()
        newList.removeAt(itemIndex)
        newList.add(itemIndex, field)
        _additionalFields.emit(newList)
    }

    private fun savePassword() {
        val passwordModel = PasswordModel(
            timestamp = System.currentTimeMillis().toString(),
            _organization.value,
            null,
            _title.value,
            _password.value,
            _additionalFields.value
        )
    }


    private fun mergeSources(
        loadingState: LoadingState,
        organization: String,
        title: String,
        password: String,
        additionalFields: List<AdditionalField>
    ): AddEditPasswordState = AddEditPasswordState(
        loadingState = loadingState,
        organization = organization,
        title = title,
        password = password,
        additionalFields = additionalFields
    )

}