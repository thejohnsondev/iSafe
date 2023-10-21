package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
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
import com.thejohnsondev.isafe.utils.TWEEN_ANIM_DEFAULT_DURATION

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Navigation() {
    val navController = rememberAnimatedNavController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screens.CheckAuthScreen.name
    ) {
        composable(
            route = Screens.CheckAuthScreen.name,
            enterTransition = null,
            popEnterTransition = null
        ) {
            val viewModel = hiltViewModel<CheckAuthStateViewModel>()
            CheckAuthStateScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.LoginScreen.name,
            enterTransition = {
                fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            }
        ) {
            val viewModel = hiltViewModel<LoginViewModel>()
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.SignUpScreen.name,
            enterTransition = {
                fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            }
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
            route = Screens.CreateEncryptionKeyScreen.name,
            enterTransition = {
                fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            }
        ) {
            val viewModel = hiltViewModel<CreateEncryptionKeyViewModel>()
            CreateEncryptionKeyScreen(navController = navController, viewModel = viewModel)
        }
        composable(
            route = Screens.EnterEncryptionKeyScreen.name,
            enterTransition = {
                fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            },
            exitTransition = {
                fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
            }
        ) {
            val viewModel = hiltViewModel<EnterEncryptionKeyViewModel>()
            EnterEncryptionKeyScreen(navController = navController, viewModel = viewModel)
        }
    }
}