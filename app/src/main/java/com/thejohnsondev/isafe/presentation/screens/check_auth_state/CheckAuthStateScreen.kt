package com.thejohnsondev.isafe.presentation.screens.check_auth_state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.thejohnsondev.isafe.presentation.navigation.Screens

@Composable
fun CheckAuthStateScreen(
    navController: NavHostController,
    viewModel: CheckAuthStateViewModel
) {
    val isUserLoggedIn = viewModel.isUserLoggedInState.collectAsState()
    isUserLoggedIn.value ?: return
    val nextScreen =
        if (isUserLoggedIn.value == true) Screens.HomeScreen.name else Screens.SignUpScreen.name
    navController.navigate(
        nextScreen,
        navOptions = NavOptions.Builder()
            .setPopUpTo(Screens.CheckAuthScreen.name, true)
            .build()
    )
}