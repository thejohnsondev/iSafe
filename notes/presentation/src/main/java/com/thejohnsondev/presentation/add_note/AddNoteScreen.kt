package com.thejohnsondev.presentation.add_note

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Text18
import com.thejohnsondev.designsystem.Text22
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.AddEditTopAppBar
import com.thejohnsondev.ui.FullScreenLoading
import com.thejohnsondev.ui.HintTextField

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AddNoteScreen(
    viewModel: AddNoteViewModel,
    goBack: () -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(AddNoteViewModel.State())
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val titleFocusRequester = remember {
        FocusRequester()
    }
    val descriptionFocusRequester = remember {
        FocusRequester()
    }
    StatusBarColor()
    LaunchedEffect(true) {
        titleFocusRequester.requestFocus()
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoToast -> context.toast(it.message)
                is OneTimeEvent.InfoSnackbar -> snackbarHostState.showSnackbar(
                    it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.SuccessNavigation -> {
                    keyboardController?.hide()
                    goBack()
                }

            }
        }
    }
    Scaffold(
        topBar = {
            AddEditTopAppBar(
                onSaveClick = {
                    viewModel.perform(AddNoteViewModel.Action.SaveNote)
                },
                onNavigateBackClick = {
                    goBack()
                })
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    modifier = Modifier
                        .padding(WindowInsets.ime.asPaddingValues())
                ) {
                    Text(text = data.visuals.message)
                }
            }
        }
    ) { paddingValues ->

        when (state.value.loadingState) {
            is LoadingState.Loading -> FullScreenLoading()
            is LoadingState.Loaded -> AddNoteContent(
                state = state.value,
                viewModel = viewModel,
                paddings = paddingValues,
                titleFocusRequester = titleFocusRequester,
                descriptionFocusRequester = descriptionFocusRequester
            )
        }

    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNoteContent(
    state: AddNoteViewModel.State,
    viewModel: AddNoteViewModel,
    paddings: PaddingValues,
    titleFocusRequester: FocusRequester,
    descriptionFocusRequester: FocusRequester,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddings)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HintTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        start = Size16,
                        end = Size16,
                        top = Size16
                    ),
                value = state.titleState,
                onValueChanged = {
                    viewModel.perform(AddNoteViewModel.Action.EnterTitle(it))
                },
                hint = stringResource(com.thejohnsondev.common.R.string.title),
                focusRequester = titleFocusRequester,
                textColor = MaterialTheme.colorScheme.onSurface,
                fontSize = Text22,
                onKeyboardAction = {
                    descriptionFocusRequester.requestFocus()
                },
                imeAction = ImeAction.Next
            )
            HintTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = Size16, vertical = Size8),
                value = state.descriptionState,
                onValueChanged = {
                    viewModel.perform(AddNoteViewModel.Action.EnterDescription(it))
                },
                hint = stringResource(com.thejohnsondev.common.R.string.note),
                textColor = MaterialTheme.colorScheme.onSurface,
                fontSize = Text18,
                focusRequester = descriptionFocusRequester,
                imeAction = ImeAction.Default,
                onKeyboardAction = {
                    keyboardController?.hide()
                }
            )

        }
    }

}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surface)
}