package com.thejohnsondev.presentation.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.presentation.create_encryption_key.CreateEncryptionKeyScreen
import com.thejohnsondev.presentation.create_encryption_key.CreateEncryptionKeyViewModel
import com.thejohnsondev.presentation.enter_encryption_key.EnterEncryptionKeyScreen
import com.thejohnsondev.presentation.enter_encryption_key.EnterEncryptionKeyViewModel

val createKeyRoute = Screens.CreateEncryptionKeyScreen.name
val enterKeyRoute = Screens.EnterEncryptionKeyScreen.name

fun NavController.navigateToCreateKey() {
    navigate(createKeyRoute)
}

fun NavController.navigateToEnterKey() {
    navigate(enterKeyRoute)
}

fun NavGraphBuilder.createKeyScreen(
    onGoToHomeScreen: () -> Unit,
    onGoToSignUpScreen: () -> Unit
) {
    composable(
        route = createKeyRoute
    ) {
        val viewModel = hiltViewModel<CreateEncryptionKeyViewModel>()
        CreateEncryptionKeyScreen(
            viewModel = viewModel,
            onGoToHomeScreen,
            onGoToSignUpScreen
        )
    }
}

fun NavGraphBuilder.enterKeyScreen(
    onGoToHomeScreen: () -> Unit,
    onGoToSignUpScreen: () -> Unit
) {
    composable(
        route = enterKeyRoute
    ) {
        val viewModel = hiltViewModel<EnterEncryptionKeyViewModel>()
        EnterEncryptionKeyScreen(
            viewModel = viewModel,
            onGoToHomeScreen,
            onGoToSignUpScreen
        )
    }
}