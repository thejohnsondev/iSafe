package com.thejohnsondev.isafe.presentation.screens.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.thejohnsondev.isafe.presentation.navigation.Screens

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel) {
    Button(onClick = {
        viewModel.logout()
        navController.navigate(Screens.SignUpScreen.name)
    }) {
        Text(text = "Logout")
    }
}