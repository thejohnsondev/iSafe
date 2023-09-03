package com.thejohnsondev.isafe.presentation.screens.feat.notes

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.domain.models.LoadingState
import com.thejohnsondev.isafe.domain.models.NoteModel
import com.thejohnsondev.isafe.domain.models.OneTimeEvent
import com.thejohnsondev.isafe.presentation.components.FullScreenLoading
import com.thejohnsondev.isafe.presentation.components.NoteItem
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.utils.Size16
import com.thejohnsondev.isafe.utils.Size32
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
        }
    ) {
        NotesContent(screenState = state.value) { note ->

        }
    }


}


@Composable
fun NotesContent(
    screenState: NotesState,
    onNoteClick: (NoteModel) -> Unit
) {
    StatusBarColor()
    Surface(
        modifier = Modifier.fillMaxSize(),
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
            Text(
                modifier = Modifier.padding(start = Size16, top = Size32),
                text = "Your notes",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(Size16))
            NotesList(notesList = screenState.notesList)
        }
    }
}

@Composable
fun NotesList(
    notesList: List<NoteModel>
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
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
        onNoteClick = {}
    )
}

