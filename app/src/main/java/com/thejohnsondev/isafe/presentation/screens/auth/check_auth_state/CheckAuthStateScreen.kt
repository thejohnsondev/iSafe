package com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.presentation.components.FullScreenLoading
import com.thejohnsondev.isafe.presentation.navigation.Screens

@Composable
fun CheckAuthStateScreen(
    navController: NavHostController,
    viewModel: CheckAuthStateViewModel
) {
    StatusBarColor()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        FullScreenLoading()
    }
    val firstScreenRoute = viewModel.firstScreenRoute.collectAsState()
    firstScreenRoute.value ?: return
    navController.navigate(
        firstScreenRoute.value!!,
        navOptions = NavOptions.Builder()
            .setPopUpTo(Screens.CheckAuthScreen.name, true)
            .build()
    )
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}