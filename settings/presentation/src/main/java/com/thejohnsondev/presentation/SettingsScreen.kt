package com.thejohnsondev.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.thejohnsondev.common.navigation.Screens

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    goToSignUp: () -> Unit
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Settings")
            Button(onClick = {
                viewModel.perform(SettingsViewModel.Action.Logout)
                    goToSignUp()
            }) {
                Text(text = "Logout")
            }
        }
    }
}