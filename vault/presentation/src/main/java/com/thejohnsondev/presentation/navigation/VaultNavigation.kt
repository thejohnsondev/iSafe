package com.thejohnsondev.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.presentation.VaultScreen
import com.thejohnsondev.presentation.VaultViewModel

val vaultRoute = Screens.VaultScreen.name

fun NavController.navigateToVault(navOptions: NavOptions? = null) {
    navigate(vaultRoute, navOptions)
}

fun NavGraphBuilder.vaultScreen(
    onAddNewPasswordClick: () -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit
) {
    composable(
        route = vaultRoute
    ) {
        val viewModel = hiltViewModel<VaultViewModel>()
        VaultScreen(
            viewModel = viewModel,
            onAddNewPasswordClick = onAddNewPasswordClick,
            onEditPasswordClick = onEditPasswordClick
        )
    }
}