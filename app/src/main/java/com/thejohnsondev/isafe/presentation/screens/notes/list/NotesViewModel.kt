package com.thejohnsondev.isafe.presentation.screens.notes.list

import com.thejohnsondev.isafe.data.local_data_source.DataStore
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.models.UserNotesResponse
import com.thejohnsondev.isafe.domain.use_cases.combined.NotesUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
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

    fun perform(action: NotesAction) {
        when (action) {
            is NotesAction.FetchNotes -> fetchNotes()
        }
    }

    private fun fetchNotes() = launchLoading {
        useCases.getAllNotes(dataStore.getUserData().id.orEmpty()).collect {
            when (it) {
                is UserNotesResponse.ResponseFailure -> handleError(it.exception)
                is UserNotesResponse.ResponseSuccess -> handleNotesList(it.notes)
            }
        }

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
    ): NotesState = NotesState(
        loadingState = loadingState,
        notesList = notesList
    )

}