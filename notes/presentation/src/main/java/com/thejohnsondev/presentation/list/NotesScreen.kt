package com.thejohnsondev.presentation.list

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.EmptyListPlaceHolder
import com.thejohnsondev.ui.ISafeLoading
import com.thejohnsondev.ui.NoteItem
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.scaffold.BottomNavItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    goToAddNote: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit,
    onNoteClick: (NoteModel) -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(NotesViewModel.State())
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }
    StatusBarColor()
    LaunchedEffect(true) {
        setScaffoldConfig(
            ScaffoldConfig(
                isTopAppBarVisible = true,
                isBottomNavBarVisible = true,
                topAppBarTitle = context.getString(R.string.your_notes),
                isFabVisible = true,
                fabTitle = context.getString(R.string.add_note),
                fabIcon = Icons.Default.Add,
                onFabClick = {
                    goToAddNote()
                },
                isFabExpanded = expandedFab,
                snackBarHostState = snackbarHostState,
                bottomBarItemIndex = BottomNavItem.Notes.index
            )
        )
        viewModel.perform(NotesViewModel.Action.FetchNotes)
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {}

            }
        }
    }
    NotesContent(
        screenState = state.value,
        state = listState,
        onNoteClick = onNoteClick
    )
}


@Composable
fun NotesContent(
    modifier: Modifier = Modifier,
    screenState: NotesViewModel.State,
    state: LazyListState,
    onNoteClick: (NoteModel) -> Unit
) {

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (screenState.loadingState is LoadingState.Loading) {
            ISafeLoading()
            return@Surface
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            NotesList(
                notesList = screenState.notesList,
                onNoteClick = onNoteClick,
                state = state
            )
        }
    }
}

@Composable
fun NotesList(
    notesList: List<NoteModel>,
    state: LazyListState,
    onNoteClick: (NoteModel) -> Unit
) {
    LazyColumn(state = state, modifier = Modifier.fillMaxWidth()) {
        item {
            if (notesList.isEmpty()) {
                EmptyListPlaceHolder(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Size32)
                )
            }
        }
        items(notesList) { note ->
            NoteItem(note = note, onNoteClicked = onNoteClick)
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}


@PreviewLightDark
@Composable
fun NotesScreenPreviewWithNotes() {
    ISafeTheme {
        NotesContent(
            screenState = NotesViewModel.State(
                loadingState = LoadingState.Loaded,
                notesList = listOf(
                    NoteModel(
                        id = "1",
                        title = "Note 1",
                        description = "Description 1",
                        lastEdit = "1711195873"
                    )
                )
            ),
            state = rememberLazyListState(),
            onNoteClick = {

            }
        )
    }
}

@PreviewLightDark
@Composable
fun NotesScreenPreviewEmptyLight() {
    ISafeTheme {
        NotesContent(
            screenState = NotesViewModel.State(
                loadingState = LoadingState.Loaded,
                notesList = listOf()
            ),
            state = rememberLazyListState(),
            onNoteClick = {

            }
        )
    }
}


@PreviewLightDark
@Composable
fun NotesScreenPreviewLoading() {
    ISafeTheme {
        NotesContent(
            screenState = NotesViewModel.State(
                loadingState = LoadingState.Loading
            ),
            state = rememberLazyListState(),
            onNoteClick = {

            }
        )
    }
}


