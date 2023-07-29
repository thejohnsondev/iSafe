package com.thejohnsondev.isafe.presentation.screens.check_auth_state

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.thejohnsondev.isafe.presentation.navigation.Screens

@Composable
fun CheckAuthStateScreen(
    navController: NavHostController,
    viewModel: CheckAuthStateViewModel
) {
    val nextScreen =
        if (viewModel.getIsUserLoggedIn()) Screens.HomeScreen.name else Screens.SignUpScreen.name
    navController.navigate(nextScreen)
}