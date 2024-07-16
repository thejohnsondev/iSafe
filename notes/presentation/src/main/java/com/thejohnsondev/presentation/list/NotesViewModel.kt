package com.thejohnsondev.presentation.list

import androidx.lifecycle.viewModelScope
import com.thejohnsondev.common.key_utils.KeyUtils
import com.thejohnsondev.common.base.BaseViewModel
import com.thejohnsondev.datastore.DataStore
import com.thejohnsondev.domain.NotesUseCases
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCases: NotesUseCases,
    private val dataStore: DataStore,
    private val keyUtils: KeyUtils
) : BaseViewModel() {

    private val _notesList = MutableStateFlow<List<NoteModel>>(emptyList())

    val state = combine(
        _loadingState,
        _notesList,
        ::mergeSources
    ).stateIn(viewModelScope, SharingStarted.Eagerly, State())

    fun perform(action: Action) {
        when (action) {
            is Action.FetchNotes -> fetchNotes()
        }
    }

    private fun fetchNotes() = launchLoading {
        useCases.getAllNotes().collect {
            it.fold(
                ifLeft = ::handleError,
                ifRight = ::handleNotesList
            )
        }
    }

    private fun handleNotesList(notesList: List<NoteModel>) = launch {
        val decryptedNotes = notesList.map {
            keyUtils.decryptNoteModel(it, dataStore.getUserKey())
        }.sortedByDescending {
            it.lastEdit
        }
        _notesList.emit(
            decryptedNotes
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