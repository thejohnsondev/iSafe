package com.thejohnsondev.isafe.presentation.screens.feat.notes

import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.use_cases.combined.NotesUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val useCases: NotesUseCases
) : BaseViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loaded)
    private val _notesList = MutableStateFlow<List<NoteModel>>(emptyList())

    val state = combine(
        _loadingState,
        _notesList,
        ::mergeSources
    )

    init {
        fetchNotes()
    }

    private fun fetchNotes() = launch {

    }

    private fun mergeSources(
        loadingState: LoadingState,
        notesList: List<NoteModel>
    ): NotesState = NotesState(
        loadingState = loadingState,
        notesList = notesList
    )

}