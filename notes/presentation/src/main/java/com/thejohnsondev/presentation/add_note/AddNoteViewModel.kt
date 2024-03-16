package com.thejohnsondev.presentation.add_note

import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.AddNoteUseCases
import com.thejohnsondev.model.DatabaseResponse
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.OneTimeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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

    fun perform(action: Action) {
        when (action) {
            is Action.EnterDescription -> enterDescription(action.description)
            is Action.EnterTitle -> enterTitle(action.title)
            is Action.SaveNote -> saveNote()
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
        )
        useCases.createNote(note).first().fold(
            ifLeft = ::handleError,
            ifRight = {
                sendEvent(OneTimeEvent.InfoToast("Note added"))
                sendEvent(OneTimeEvent.SuccessNavigation)
            }
        )
    }

    private fun mergeSources(
        loadingState: LoadingState,
        titleState: String,
        descriptionState: String
    ): State = State(
        loadingState,
        titleState,
        descriptionState
    )

    sealed class Action {
        class EnterTitle(val title: String) : Action()
        class EnterDescription(val description: String) : Action()
        object SaveNote : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val titleState: String = EMPTY,
        val descriptionState: String = EMPTY
    )

}