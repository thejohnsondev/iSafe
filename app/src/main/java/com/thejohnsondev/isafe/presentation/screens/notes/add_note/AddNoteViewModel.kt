package com.thejohnsondev.isafe.presentation.screens.notes.add_note

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.DatabaseResponse
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.domain.use_cases.combined.AddNoteUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val useCases: AddNoteUseCases,
    private val dataStore: DataStore
) : BaseViewModel() {

    private val _titleState = MutableStateFlow("")
    private val _descriptionState = MutableStateFlow("")

    val state = combine(
        _loadingState,
        _titleState,
        _descriptionState,
        ::mergeSources
    )

    fun perform(action: AddNoteAction) {
        when (action) {
            is AddNoteAction.EnterDescription -> enterDescription(action.description)
            is AddNoteAction.EnterTitle -> enterTitle(action.title)
            is AddNoteAction.SaveNote -> saveNote()
        }
    }

    private fun enterTitle(title: String) = launch {
        _titleState.emit(title)
    }

    private fun enterDescription(description: String) = launch {
        _descriptionState.emit(description)
    }

    private fun saveNote() = launchLoading {
        val note = NoteModel(
            id = System.currentTimeMillis().toString(),
            title = _titleState.value,
            description = _descriptionState.value,
            category = ""
        )
        useCases.createNote(dataStore.getUserData().id.orEmpty(), note)
            .collect {
                when (it) {
                    is DatabaseResponse.ResponseFailure -> it.exception?.message?.let { message ->
                        sendEvent(
                            OneTimeEvent.InfoToast(
                                message
                            )
                        )
                    }

                    is DatabaseResponse.ResponseSuccess -> {
                        sendEvent(OneTimeEvent.InfoToast("Note added"))
                        sendEvent(OneTimeEvent.SuccessNavigation)
                    }
                }
            }
    }

    private fun mergeSources(
        loadingState: LoadingState,
        titleState: String,
        descriptionState: String
    ): AddNoteState = AddNoteState(
        loadingState,
        titleState,
        descriptionState
    )

}