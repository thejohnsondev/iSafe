package com.thejohnsondev.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.presentation.SettingsScreen
import com.thejohnsondev.presentation.SettingsViewModel

val settingsRoute = Screens.Settings.name

fun NavController.navigateToSettings() {
    navigate(settingsRoute)
}

fun NavGraphBuilder.settingsScreen(
    goToSignUp: () -> Unit
) {
    composable(
        route = settingsRoute
    ) {
        val viewModel = hiltViewModel<SettingsViewModel>()
        SettingsScreen(
            viewModel = viewModel,
            goToSignUp = goToSignUp
        )
    }
}