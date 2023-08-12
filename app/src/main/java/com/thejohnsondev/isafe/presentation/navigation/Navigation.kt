package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.thejohnsondev.isafe.presentation.screens.check_auth_state.CheckAuthStateScreen
import com.thejohnsondev.isafe.presentation.screens.check_auth_state.CheckAuthStateViewModel
import com.thejohnsondev.isafe.presentation.screens.home.HomeScreen
import com.thejohnsondev.isafe.presentation.screens.login.LoginScreen
import com.thejohnsondev.isafe.presentation.screens.login.LoginViewModel
import com.thejohnsondev.isafe.presentation.screens.signup.SignUpScreen
import com.thejohnsondev.isafe.presentation.screens.signup.SignUpViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.CheckAuthScreen.name
    ) {
        composable(route = Screens.CheckAuthScreen.name) {
            val viewModel = hiltViewModel<CheckAuthStateViewModel>()
            CheckAuthStateScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screens.LoginScreen.name,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Companion.Left,
                    animationSpec = tween(350)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Companion.Right,
                    animationSpec = tween(350)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentScope.SlideDirection.Companion.Left,
                    animationSpec = tween(350)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentScope.SlideDirection.Companion.Right,
                    animationSpec = tween(350)
                )
            }) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.SignUpScreen.name
        ) {
            val viewModel = hiltViewModel<SignUpViewModel>()
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = Screens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
    }
}