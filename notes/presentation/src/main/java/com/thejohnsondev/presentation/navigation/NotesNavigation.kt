package com.thejohnsondev.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.presentation.add_note.AddNoteScreen
import com.thejohnsondev.presentation.add_note.AddNoteViewModel
import com.thejohnsondev.presentation.list.NotesScreen
import com.thejohnsondev.presentation.list.NotesViewModel

val addNoteRoute = Screens.AddNote.name
val notesRoute = Screens.NotesScreen.name

fun NavController.navigateToAddNote() {
    navigate(addNoteRoute)
}

fun NavController.navigateToNotes() {
    navigate(notesRoute)
}

fun NavGraphBuilder.addNoteScreen(
    goBack: () -> Unit
) {
    composable(
        route = addNoteRoute
    ) {
        val viewModel = hiltViewModel<AddNoteViewModel>()
        AddNoteScreen(
            viewModel = viewModel,
            goBack = goBack
        )
    }
}

fun NavGraphBuilder.notesScreen(
    goToAddNote: () -> Unit
) {
    composable(
        route = notesRoute
    ) {
        val viewModel = hiltViewModel<NotesViewModel>()
        NotesScreen(
            viewModel = viewModel,
            goToAddNote = goToAddNote
        )
    }
}