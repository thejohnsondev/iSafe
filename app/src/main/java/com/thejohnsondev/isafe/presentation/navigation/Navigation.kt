package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thejohnsondev.isafe.presentation.screens.home.HomeScreen
import com.thejohnsondev.presentation.nagivation.checkAuthStateRoute
import com.thejohnsondev.presentation.nagivation.checkAuthStateScreen
import com.thejohnsondev.presentation.nagivation.loginScreen
import com.thejohnsondev.presentation.nagivation.navigateToLogin
import com.thejohnsondev.presentation.nagivation.signUpScreen
import com.thejohnsondev.presentation.navigation.createKeyScreen
import com.thejohnsondev.presentation.navigation.enterKeyScreen
import com.thejohnsondev.presentation.navigation.navigateToCreateKey
import com.thejohnsondev.presentation.navigation.navigateToEnterKey

@Composable
fun Navigation() {
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
            goToEnterKey = {
                navController.navigateToEnterKey()
            },
            goBack = {
                navController.popBackStack()
            }
        )
        signUpScreen(
            goToCreateKey = {
                navController.navigateToCreateKey()
            },
            goToLogin = {
                navController.navigateToLogin()
            }
        )
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
        composable(
            route = Screens.HomeScreen.name
        ) {
            HomeScreen(rootNavController = navController)
        }
    }
}