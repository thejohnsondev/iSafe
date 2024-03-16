package com.thejohnsondev.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.thejohnsondev.common.toast
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.tools.ToolModel
import com.thejohnsondev.ui.ScaffoldConfig

@Composable
fun ToolsScreen(
    viewModel: ToolsViewModel,
    onToolClicked: (String) -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsState(ToolsViewModel.State())
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(true) {
        viewModel.perform(ToolsViewModel.Action.FetchTools)
        viewModel.getEventFlow().collect {
            when (it) {
                is OneTimeEvent.InfoSnackbar -> snackBarHostState.showSnackbar(
                    message = it.message,
                    duration = SnackbarDuration.Short
                )

                is OneTimeEvent.InfoToast -> context.toast(it.message)
                OneTimeEvent.SuccessNavigation -> {}
            }
        }
    }
    setScaffoldConfig(
        ScaffoldConfig(
            isTopAppBarVisible = true,
            isBottomNavBarVisible = true,
            topAppBarTitle = stringResource(com.thejohnsondev.common.R.string.tools),
            snackBarHostState = snackBarHostState
        )
    )
    ToolsContent(
        state = state.value,
        onToolClicked = onToolClicked
    )
}

@Composable
fun ToolsContent(
    modifier: Modifier = Modifier,
    state: ToolsViewModel.State,
    onToolClicked: (String) -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

    }
}

@Preview
@Composable
private fun ToolsPreview() {
    ISafeTheme {
        ToolsContent(
            state = ToolsViewModel.State(
                toolsList = listOf(
                    ToolModel(
                        id = "1",
                        name = "Tool 1",
                        description = "Description 1",
                        icon = "https://www.example.com/image1.png",
                        url = "https://www.example.com/tool1"
                    )
                )
            ),
            onToolClicked = {}
        )
    }

}