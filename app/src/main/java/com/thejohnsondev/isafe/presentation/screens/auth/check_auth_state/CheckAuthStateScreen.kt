package com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state

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
    val firstScreenRoute = viewModel.firstScreenRoute.collectAsState()
    firstScreenRoute.value ?: return
    navController.navigate(
        firstScreenRoute.value!!,
        navOptions = NavOptions.Builder()
            .setPopUpTo(Screens.CheckAuthScreen.name, true)
            .build()
    )
}