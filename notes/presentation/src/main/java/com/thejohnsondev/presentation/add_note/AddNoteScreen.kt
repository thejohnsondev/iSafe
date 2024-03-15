package com.thejohnsondev.presentation.add_note

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Text18
import com.thejohnsondev.designsystem.Text22
import com.thejohnsondev.model.LoadingState
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.ui.ISafeLoading
import com.thejohnsondev.ui.HintTextField
import com.thejohnsondev.ui.ScaffoldConfig

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNoteScreen(
    viewModel: AddNoteViewModel,
    goBack: () -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
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
    setScaffoldConfig(
        ScaffoldConfig(
            isBottomNavBarVisible = false,
            isTopAppBarVisible = true,
            topAppBarIcon = Icons.Default.ArrowBack,
            isTopAppBarSaveButtonVisible = true,
            onTopAppBarSaveClick = {
                viewModel.perform(AddNoteViewModel.Action.SaveNote)
            },
            onTopAppBarIconClick = {
                goBack()
            },
            snackBarHostState = snackbarHostState,
        )
    )

    when (state.value.loadingState) {
        is LoadingState.Loading -> ISafeLoading()
        is LoadingState.Loaded -> AddNoteContent(
            state = state.value,
            titleFocusRequester = titleFocusRequester,
            descriptionFocusRequester = descriptionFocusRequester
        )
    }


}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddNoteContent(
    state: AddNoteViewModel.State,
    titleFocusRequester: FocusRequester,
    descriptionFocusRequester: FocusRequester,
    onEnterTitle: (String) -> Unit = {},
    onEnterDescription: (String) -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
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
                onValueChanged = { title ->
                    onEnterTitle(title)
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
                onValueChanged = { description ->
                    onEnterDescription(description)
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AddNoteScreenPreviewEmptyLight() {
    ISafeTheme {
        AddNoteContent(
            state = AddNoteViewModel.State(),
            titleFocusRequester = FocusRequester(),
            descriptionFocusRequester = FocusRequester()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddNoteScreenPreviewEmptyDark() {
    ISafeTheme {
        AddNoteContent(
            state = AddNoteViewModel.State(),
            titleFocusRequester = FocusRequester(),
            descriptionFocusRequester = FocusRequester()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun AddNoteScreenPreviewWithDataLight() {
    ISafeTheme {
        AddNoteContent(
            state = AddNoteViewModel.State(
                titleState = "Some title",
                descriptionState = "Some long description lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
            ),
            titleFocusRequester = FocusRequester(),
            descriptionFocusRequester = FocusRequester()
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun AddNoteScreenPreviewWithDataDark() {
    ISafeTheme {
        AddNoteContent(
            state = AddNoteViewModel.State(
                titleState = "Some title",
                descriptionState = "Some long description lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum"
            ),
            titleFocusRequester = FocusRequester(),
            descriptionFocusRequester = FocusRequester()
        )
    }
}