package com.thejohnsondev.presentation.add_note

import com.thejohnsondev.common.EMPTY
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.common.encryptModel
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

    private val _noteId = MutableStateFlow<String?>(null)
    private val _titleState = MutableStateFlow("")
    private val _descriptionState = MutableStateFlow("")
    private val _isEdit = MutableStateFlow(false)

    val state = combine(
        _loadingState,
        _titleState,
        _descriptionState,
        _isEdit,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.EnterDescription -> enterDescription(action.description)
            is Action.EnterTitle -> enterTitle(action.title)
            is Action.SaveNote -> saveNote()
            is Action.SetNoteModelForEdit -> setNoteModelForEdit(action.noteModel)
            is Action.DeleteNote -> deleteNote()
        }
    }

    private fun deleteNote() = launchLoading {
        _noteId.value?.let {
            useCases.deleteNote(it).first().fold(
                ifLeft = ::handleError,
                ifRight = {
                    sendEvent(OneTimeEvent.InfoToast("Note deleted"))
                    sendEvent(OneTimeEvent.SuccessNavigation())
                }
            )
        }
    }

    private fun setNoteModelForEdit(noteModel: NoteModel) = launch {
        enterTitle(noteModel.title)
        enterDescription(noteModel.description)
        _noteId.emit(noteModel.id)
        _isEdit.emit(true)
    }

    private fun enterTitle(title: String) = launch {
        _titleState.emit(title)
    }

    private fun enterDescription(description: String) = launch {
        _descriptionState.emit(description)
    }

    private fun saveNote() = launchLoading {
        val note = NoteModel(
            id = _noteId.value,
            title = _titleState.value,
            description = _descriptionState.value,
            lastEdit = System.currentTimeMillis().toString()
        )
        val encryptedNote = note.encryptModel(dataStore.getUserKey())
        if (_isEdit.value) {
            useCases.updateNote(encryptedNote).first().fold(
                ifLeft = ::handleError,
                ifRight = {
                    sendEvent(OneTimeEvent.InfoToast("Note updated"))
                    sendEvent(OneTimeEvent.SuccessNavigation())
                }
            )
            return@launchLoading
        } else {
            useCases.createNote(encryptedNote).first().fold(
                ifLeft = ::handleError,
                ifRight = {
                    sendEvent(OneTimeEvent.InfoToast("Note added"))
                    sendEvent(OneTimeEvent.SuccessNavigation())
                }
            )
        }
    }

    private fun mergeSources(
        loadingState: LoadingState,
        titleState: String,
        descriptionState: String,
        isEdit: Boolean
    ): State = State(
        loadingState,
        titleState,
        descriptionState,
        isEdit
    )

    sealed class Action {
        class EnterTitle(val title: String) : Action()
        class EnterDescription(val description: String) : Action()
        class SetNoteModelForEdit(val noteModel: NoteModel) : Action()
        object SaveNote : Action()
        object DeleteNote : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val titleState: String = EMPTY,
        val descriptionState: String = EMPTY,
        val isEdit: Boolean = false
    )

}