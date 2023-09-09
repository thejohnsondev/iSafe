package com.thejohnsondev.isafe.presentation.screens.feat.notes.add_note

import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.use_cases.combined.AddNoteUseCases
import com.thejohnsondev.isafe.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val useCases: AddNoteUseCases
) : BaseViewModel() {

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loaded)
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

    private fun saveNote() {

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