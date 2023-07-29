package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thejohnsondev.isafe.presentation.screens.check_auth_state.CheckAuthStateScreen
import com.thejohnsondev.isafe.presentation.screens.check_auth_state.CheckAuthStateViewModel
import com.thejohnsondev.isafe.presentation.screens.home.HomeScreen
import com.thejohnsondev.isafe.presentation.screens.login.LoginScreen
import com.thejohnsondev.isafe.presentation.screens.signup.SignUpScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.CheckAuthScreen.name) {
        composable(Screens.CheckAuthScreen.name) {
            val viewModel = hiltViewModel<CheckAuthStateViewModel>()
            CheckAuthStateScreen(navController = navController, viewModel = viewModel)
        }
        composable(Screens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(Screens.SignUpScreen.name) {
            SignUpScreen(navController = navController)
        }
        composable(Screens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
    }
}