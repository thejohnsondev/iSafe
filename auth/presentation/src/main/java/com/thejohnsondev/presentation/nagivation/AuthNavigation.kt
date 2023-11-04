package com.thejohnsondev.presentation.nagivation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.presentation.check_auth_state.CheckAuthStateScreen
import com.thejohnsondev.presentation.check_auth_state.CheckAuthStateViewModel
import com.thejohnsondev.presentation.login.LoginScreen
import com.thejohnsondev.presentation.login.LoginViewModel
import com.thejohnsondev.presentation.signup.SignUpScreen
import com.thejohnsondev.presentation.signup.SignUpViewModel

val checkAuthStateRoute = Screens.CheckAuthScreen.name
val loginRoute = Screens.LoginScreen.name
val signUpRoute = Screens.SignUpScreen.name

fun NavController.navigateToCheckAuthState() {
    navigate(checkAuthStateRoute)
}

fun NavController.navigateToLogin() {
    navigate(loginRoute)
}

fun NavController.navigateToSignUp() {
    navigate(
        signUpRoute,
        NavOptions.Builder()
            .setPopUpTo(signUpRoute, true)
            .build()
    )
}

fun NavGraphBuilder.checkAuthStateScreen(
    goToScreen: (String, NavOptions) -> Unit
) {
    composable(
        route = checkAuthStateRoute
    ) {
        val viewModel = hiltViewModel<CheckAuthStateViewModel>()
        CheckAuthStateScreen(
            viewModel = viewModel,
            goToScreen = goToScreen
        )
    }
}

fun NavGraphBuilder.loginScreen(
    goToEnterKey: () -> Unit,
    goBack: () -> Unit
) {
    composable(
        route = loginRoute
    ) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(
            viewModel = viewModel,
            goToEnterKey = goToEnterKey,
            goBack = goBack
        )
    }
}

fun NavGraphBuilder.signUpScreen(
    goToCreateKey: () -> Unit,
    goToLogin: () -> Unit
) {
    composable(
        route = signUpRoute
    ) {
        val viewModel = hiltViewModel<SignUpViewModel>()
        SignUpScreen(
            viewModel = viewModel,
            goToCreateKey = goToCreateKey,
            goToLogin = goToLogin
        )
    }
}