package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state.CheckAuthStateScreen
import com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state.CheckAuthStateViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.login.LoginScreen
import com.thejohnsondev.isafe.presentation.screens.auth.login.LoginViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.signup.SignUpScreen
import com.thejohnsondev.isafe.presentation.screens.auth.signup.SignUpViewModel
import com.thejohnsondev.isafe.presentation.screens.home.HomeScreen
import com.thejohnsondev.presentation.navigation.createKeyScreen
import com.thejohnsondev.presentation.navigation.enterKeyScreen
import com.thejohnsondev.presentation.navigation.navigateToCreateKey
import com.thejohnsondev.presentation.navigation.navigateToEnterKey

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.CheckAuthScreen.name
    ) {
        composable(
            route = Screens.CheckAuthScreen.name
        ) {
            val viewModel = hiltViewModel<CheckAuthStateViewModel>()
            CheckAuthStateScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.LoginScreen.name
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel, onGoToEnterKeyScreen = {
                navController.navigateToEnterKey()
            })
        }
        composable(
            route = Screens.SignUpScreen.name
        ) {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(
                navController = navController,
                viewModel = viewModel,
                onGoToCreateKeyScreen = {
                    navController.navigateToCreateKey()
                })
        }
        composable(
            route = Screens.HomeScreen.name
        ) {
            HomeScreen(rootNavController = navController)
        }
        createKeyScreen(
            onGoToHomeScreen = {
                navController.navigate(Screens.HomeScreen.name)
            },
            onGoToSignUpScreen = {
                navController.navigate(Screens.SignUpScreen.name)
            }
        )
        enterKeyScreen(
            onGoToHomeScreen = {
                navController.navigate(Screens.HomeScreen.name)
            },
            onGoToSignUpScreen = {
                navController.navigate(Screens.SignUpScreen.name)
            }
        )
    }
}