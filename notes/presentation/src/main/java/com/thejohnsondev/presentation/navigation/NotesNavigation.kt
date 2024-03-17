package com.thejohnsondev.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.fromJson
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.presentation.add_note.AddNoteScreen
import com.thejohnsondev.presentation.add_note.AddNoteViewModel
import com.thejohnsondev.presentation.list.NotesScreen
import com.thejohnsondev.presentation.list.NotesViewModel
import com.thejohnsondev.ui.ScaffoldConfig

val addNoteRoute = Screens.AddNote.name
val notesRoute = Screens.NotesScreen.name

fun NavController.navigateToAddNote(note: String?, navOptions: NavOptions? = null) {
    navigate("$addNoteRoute/$note", navOptions)
}

fun NavGraphBuilder.addNoteScreen(
    goBack: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    composable(
        route = "$addNoteRoute/{note}"
    ) { navBackStackEntry ->
        val viewModel = hiltViewModel<AddNoteViewModel>()
        val noteModel = navBackStackEntry.arguments?.getString("note")
            .fromJson<NoteModel?>()
        AddNoteScreen(
            viewModel = viewModel,
            noteModel = noteModel,
            goBack = goBack,
            setScaffoldConfig = setScaffoldConfig
        )
    }
}

fun NavGraphBuilder.notesScreen(
    goToAddNote: () -> Unit,
    onNoteClick: (NoteModel) -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit,
) {
    composable(
        route = notesRoute
    ) {
        val viewModel = hiltViewModel<NotesViewModel>()
        NotesScreen(
            viewModel = viewModel,
            goToAddNote = goToAddNote,
            onNoteClick = onNoteClick,
            setScaffoldConfig = setScaffoldConfig
        )
    }
}