package com.thejohnsondev.isafe.presentation.screens.feat.notes.list

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
import com.thejohnsondev.isafe.R
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.presentation.components.FullScreenLoading
import com.thejohnsondev.isafe.presentation.components.NoteItem
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size86
import com.thejohnsondev.isafe.utils.toast

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    navController: NavHostController,
    viewModel: NotesViewModel,
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(NotesState())
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
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    navController.navigate(Screens.CreateEncryptionKeyScreen.name)
                }

            }
        }
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(bottom = Size86, start = Size16, end = Size16),
                ) {
                    Text(text = data.visuals.message)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.your_notes),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                    )
                },
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    navController.navigate(Screens.AddNote.name)
                },
                expanded = expandedFab,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.add)
                    )
                },
                text = {
                    Text(text = stringResource(R.string.add_note))
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        NotesContent(
            modifier = Modifier.padding(paddingValues),
            screenState = state.value,
            state = listState
        ) { note ->

        }
    }


}


@Composable
fun NotesContent(
    modifier: Modifier = Modifier,
    screenState: NotesState,
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


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreview() {
    NotesContent(
        screenState = NotesState(
            loadingState = LoadingState.Loaded,
            notesList = emptyList()
        ),
        state = rememberLazyListState(),
        onNoteClick = {

        }
    )
}

