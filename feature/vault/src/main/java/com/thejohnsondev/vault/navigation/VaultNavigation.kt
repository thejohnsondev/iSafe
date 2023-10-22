package com.thejohnsondev.vault.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import com.google.accompanist.navigation.animation.composable
import com.thejohnsondev.common.TWEEN_ANIM_DEFAULT_DURATION
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.model.PasswordModel
import com.thejohnsondev.vault.VaultScreen
import com.thejohnsondev.vault.VaultViewModel

val vaultRoute = Screens.VaultScreen.name

fun NavController.navigateToVault(navOptions: NavOptions? = null) {
    navigate(vaultRoute, navOptions)
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.vaultScreen(
    onAddNewPasswordClick: () -> Unit,
    onEditPasswordClick: (PasswordModel) -> Unit
) {
    composable(
        route = vaultRoute,
        enterTransition = {
            fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
        }
    ) {
        val viewModel = hiltViewModel<VaultViewModel>()
        VaultScreen(
            viewModel = viewModel,
            onAddNewPasswordClick = onAddNewPasswordClick,
            onEditPasswordClick = onEditPasswordClick
        )
    }
}