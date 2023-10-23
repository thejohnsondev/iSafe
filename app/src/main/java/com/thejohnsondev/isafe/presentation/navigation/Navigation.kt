package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state.CheckAuthStateScreen
import com.thejohnsondev.isafe.presentation.screens.auth.check_auth_state.CheckAuthStateViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.create_encryption_key.CreateEncryptionKeyScreen
import com.thejohnsondev.isafe.presentation.screens.auth.create_encryption_key.CreateEncryptionKeyViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.enter_encryption_key.EnterEncryptionKeyScreen
import com.thejohnsondev.isafe.presentation.screens.auth.enter_encryption_key.EnterEncryptionKeyViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.login.LoginScreen
import com.thejohnsondev.isafe.presentation.screens.auth.login.LoginViewModel
import com.thejohnsondev.isafe.presentation.screens.auth.signup.SignUpScreen
import com.thejohnsondev.isafe.presentation.screens.auth.signup.SignUpViewModel
import com.thejohnsondev.isafe.presentation.screens.home.HomeScreen

@OptIn(ExperimentalAnimationApi::class)
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
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.SignUpScreen.name
        ) {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.HomeScreen.name
        ) {
            HomeScreen(rootNavController = navController)
        }
        composable(
            route = Screens.CreateEncryptionKeyScreen.name
        ) {
            val viewModel = hiltViewModel<CreateEncryptionKeyViewModel>()
            CreateEncryptionKeyScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.EnterEncryptionKeyScreen.name
        ) {
            val viewModel = hiltViewModel<EnterEncryptionKeyViewModel>()
            EnterEncryptionKeyScreen(navController = navController, viewModel = viewModel)
        }
    }
}