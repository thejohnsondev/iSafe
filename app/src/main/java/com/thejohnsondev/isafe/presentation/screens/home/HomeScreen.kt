package com.thejohnsondev.isafe.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.isafe.domain.models.PasswordModel
import com.thejohnsondev.isafe.presentation.navigation.ISafeBottomNavigation
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.presentation.screens.notes.add_note.AddNoteScreen
import com.thejohnsondev.isafe.presentation.screens.notes.add_note.AddNoteViewModel
import com.thejohnsondev.isafe.presentation.screens.notes.list.NotesScreen
import com.thejohnsondev.isafe.presentation.screens.notes.list.NotesViewModel
import com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password.AddEditPasswordScreen
import com.thejohnsondev.isafe.presentation.screens.passwords.add_edit_password.AddEditPasswordViewModel
import com.thejohnsondev.isafe.presentation.screens.settings.SettingsScreen
import com.thejohnsondev.isafe.presentation.screens.settings.SettingsViewModel
import com.thejohnsondev.isafe.utils.fromJson
import com.thejohnsondev.isafe.utils.toJson
import com.thejohnsondev.vault.navigation.vaultRoute
import com.thejohnsondev.vault.navigation.vaultScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(rootNavController: NavController) {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()

    when (navBackStackEntry?.destination?.route) {
        Screens.AddNote.name,
        Screens.AddEditPassword.name -> {
            bottomBarState.value = false
        }
        else -> {
            bottomBarState.value = true
        }
    }

    StatusBarColor()
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Scaffold(
            bottomBar = {
                ISafeBottomNavigation(
                    navController = navController,
                    bottomBarState = bottomBarState
                )
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = vaultRoute,
                modifier = Modifier.padding(it)
            ) {
                vaultScreen(
                    onAddNewPasswordClick = {
                        // TODO: implement
                        navController.navigate("${Screens.AddEditPassword.name}/null")
                    },
                    onEditPasswordClick = {
                        navController.navigate("${Screens.AddEditPassword.name}/${it.toJson()}\"")
                        // TODO: implement
                    }
                )
                composable(
                    route = Screens.NotesScreen.name
                ) {
                    val viewModel = hiltViewModel<NotesViewModel>()
                    NotesScreen(navController = navController, viewModel = viewModel)
                }
                composable(
                    route = Screens.Settings.name
                ) {
                    val viewModel = hiltViewModel<SettingsViewModel>()
                    SettingsScreen(
                        rootNavController = rootNavController,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = Screens.AddNote.name
                ) {
                    val viewModel = hiltViewModel<AddNoteViewModel>()
                    AddNoteScreen(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = "${Screens.AddEditPassword.name}/{password}"
                ) { navBackStackEntry ->
                    val passwordModel = navBackStackEntry.arguments?.getString("password")
                        .fromJson<PasswordModel?>()
                    val viewModel = hiltViewModel<AddEditPasswordViewModel>()
                    AddEditPasswordScreen(
                        navController = navController,
                        viewModel = viewModel,
                        passwordModel = passwordModel
                    )
                }
            }
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}