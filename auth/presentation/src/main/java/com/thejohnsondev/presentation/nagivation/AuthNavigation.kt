package com.thejohnsondev.presentation.nagivation

import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.presentation.biometric.BiometricScreen
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
    navigate(
        loginRoute,
        NavOptions.Builder()
            .setPopUpTo(loginRoute, true)
            .build()
    )
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
    goToHome: () -> Unit,
    goBack: () -> Unit
) {
    composable(
        route = loginRoute
    ) {
        val viewModel = hiltViewModel<LoginViewModel>()
        LoginScreen(
            viewModel = viewModel,
            goToHome = goToHome,
            goBack = goBack
        )
    }
}

fun NavGraphBuilder.signUpScreen(
    goToHome: () -> Unit,
    goToLogin: () -> Unit
) {
    composable(
        route = signUpRoute
    ) {
        val viewModel = hiltViewModel<SignUpViewModel>()
        SignUpScreen(
            viewModel = viewModel,
            goToHome = goToHome,
            goToLogin = goToLogin
        )
    }
}

fun NavGraphBuilder.biometricScreen(
    parentActivity: FragmentActivity,
    onBiometricSuccess: () -> Unit
) {
    composable(
        route = Screens.BiometricScreen.name
    ) {
        BiometricScreen(
            parentActivity = parentActivity,
            onBiometricSuccess = onBiometricSuccess
        )
    }
}