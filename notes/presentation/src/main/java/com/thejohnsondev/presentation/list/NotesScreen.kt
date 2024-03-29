package com.thejohnsondev.presentation.list

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.R
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.NoteModel
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.FullScreenLoading
import com.thejohnsondev.ui.NoteItem
import com.thejohnsondev.ui.ScaffoldConfig

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    viewModel: NotesViewModel,
    goToAddNote: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
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
//        viewModel.perform(NotesViewModel.Action.FetchNotes)       todo uncomment it when api is ready
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
    setScaffoldConfig(
        ScaffoldConfig(
            isTopAppBarVisible = true,
            isBottomNavBarVisible = true,
            topAppBarTitle = stringResource(R.string.your_notes),
            isFabVisible = true,
            fabTitle = stringResource(R.string.add_note),
            fabIcon = Icons.Default.Add,
            onFabClick = {
                goToAddNote()
            },
            isFabExpanded = expandedFab,
            snackBarHostState = snackbarHostState,
        )
    )
    NotesContent(
        screenState = state.value,
        state = listState
    ) { note ->

    }
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
            FullScreenLoading()
            return@Surface
        }
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(Size16))
            NotesList(notesList = screenState.notesList, state = state)
        }
    }
}

@Composable
fun NotesList(
    notesList: List<NoteModel>,
    state: LazyListState
) {
    LazyColumn(state = state, modifier = Modifier.fillMaxWidth()) {
        items(notesList) { note ->
            NoteItem(note = note) { clickedNote ->
                // handle click
            }
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}


@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun NotesScreenPreviewWithNotesLight() {
    ISafeTheme {
        NotesContent(
            screenState = NotesViewModel.State(
                loadingState = LoadingState.Loaded,
                notesList = listOf(
                    NoteModel(
                        id = "1",
                        title = "Note 1",
                        description = "Description 1",
                        category = "Category 1",
                    )
                )
            ),
            state = rememberLazyListState(),
            onNoteClick = {

            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreviewWithNotesDark() {
    ISafeTheme {
        NotesContent(
            screenState = NotesViewModel.State(
                loadingState = LoadingState.Loaded,
                notesList = listOf(
                    NoteModel(
                        id = "1",
                        title = "Note 1",
                        description = "Description 1",
                        category = "Category 1",
                    )
                )
            ),
            state = rememberLazyListState(),
            onNoteClick = {

            }
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreviewEmptyDark() {
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun NotesScreenPreviewLoadingLight() {
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreviewLoadingDark() {
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


