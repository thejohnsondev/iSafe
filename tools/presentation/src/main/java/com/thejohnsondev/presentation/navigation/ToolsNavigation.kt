package com.thejohnsondev.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.presentation.ToolsScreen
import com.thejohnsondev.presentation.ToolsViewModel
import com.thejohnsondev.ui.ScaffoldConfig

val toolsRoute = Screens.Tools.name

fun NavController.navigateToTools(navOptions: NavOptions? = null) {
    navigate(toolsRoute, navOptions)
}

fun NavGraphBuilder.toolsScreen(
    onNavigateToTool: (String) -> Unit,
    setScaffoldConfig: (ScaffoldConfig) -> Unit
) {
    composable(
        route = toolsRoute
    ) {
        val viewModel = hiltViewModel<ToolsViewModel>()
        ToolsScreen(
            viewModel = viewModel,
            setScaffoldConfig = setScaffoldConfig,
            onToolClicked = onNavigateToTool
        )
    }

}