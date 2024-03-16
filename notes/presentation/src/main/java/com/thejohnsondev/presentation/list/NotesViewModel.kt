package com.thejohnsondev.presentation.list

import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.NotesUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.UserNotesResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCases: NotesUseCases,
    private val dataStore: DataStore
) : BaseViewModel() {

    private val _notesList = MutableStateFlow<List<NoteModel>>(emptyList())

    val state = combine(
        _loadingState,
        _notesList,
        ::mergeSources
    )

    fun perform(action: Action) {
        when (action) {
            is Action.FetchNotes -> fetchNotes()
        }
    }

    private fun fetchNotes() = launchLoading {
        useCases.getAllNotes().first().fold(
            ifLeft = ::handleError,
            ifRight = ::handleNotesList
        )

    }

    private fun handleNotesList(notesList: List<NoteModel>) = launch {
        _notesList.emit(
            notesList
        )
        loaded()
    }

    private fun mergeSources(
        loadingState: LoadingState,
        notesList: List<NoteModel>
    ): State = State(
        loadingState = loadingState,
        notesList = notesList
    )

    sealed class Action {
        object FetchNotes : Action()
    }

    data class State(
        val loadingState: LoadingState = LoadingState.Loaded,
        val notesList: List<NoteModel> = emptyList()
    )

}