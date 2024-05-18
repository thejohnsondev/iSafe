package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.isafe.presentation.navigation.home.HomeNavigation
import com.thejohnsondev.presentation.nagivation.biometricScreen
import com.thejohnsondev.presentation.nagivation.checkAuthStateRoute
import com.thejohnsondev.presentation.nagivation.checkAuthStateScreen
import com.thejohnsondev.presentation.nagivation.loginScreen
import com.thejohnsondev.presentation.nagivation.navigateToLogin
import com.thejohnsondev.presentation.nagivation.signUpScreen

@Composable
fun Navigation(
    parentActivity: FragmentActivity
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = checkAuthStateRoute
    ) {
        checkAuthStateScreen(
            goToScreen = { screen, navOptions ->
                navController.navigate(screen, navOptions)
            }
        )
        loginScreen(
            goToHome = {
                navController.navigate(Screens.HomeScreen.name)
            },
            goBack = {
                navController.popBackStack()
            }
        )
        signUpScreen(
            goToHome = {
                navController.navigate(Screens.HomeScreen.name)
            },
            goToLogin = {
                navController.navigateToLogin()
            }
        )
        biometricScreen(
            parentActivity = parentActivity,
            onBiometricSuccess = {
                navController.navigate(Screens.HomeScreen.name)
            }
        )
        composable(
            route = Screens.HomeScreen.name
        ) {
            HomeNavigation(rootNavController = navController)
        }
    }
}