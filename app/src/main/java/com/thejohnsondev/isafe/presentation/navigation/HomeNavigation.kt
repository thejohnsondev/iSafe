package com.thejohnsondev.isafe.presentation.navigation

import android.annotation.SuppressLint
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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.common.toJson
import com.thejohnsondev.presentation.nagivation.navigateToSignUp
import com.thejohnsondev.presentation.navigation.addEditPasswordScreen
import com.thejohnsondev.presentation.navigation.addNoteScreen
import com.thejohnsondev.presentation.navigation.navigateToAddEditPassword
import com.thejohnsondev.presentation.navigation.navigateToAddNote
import com.thejohnsondev.presentation.navigation.notesScreen
import com.thejohnsondev.presentation.navigation.settingsScreen
import com.thejohnsondev.presentation.navigation.vaultRoute
import com.thejohnsondev.presentation.navigation.vaultScreen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNavigation(rootNavController: NavController) {
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
                        navController.navigateToAddEditPassword(password = null)
                    },
                    onEditPasswordClick = {
                        navController.navigateToAddEditPassword(password = it.toJson())
                    }
                )
                addEditPasswordScreen(
                    onGoBackClick = {
                        navController.popBackStack()
                    }
                )
                addNoteScreen(
                    goBack = {
                        navController.popBackStack()
                    }
                )
                notesScreen(
                    goToAddNote = {
                        navController.navigateToAddNote()
                    }
                )
                settingsScreen(
                    goToSignUp = {
                        rootNavController.navigateToSignUp()
                    }
                )
            }
        }
    }
}

@Composable
fun StatusBarColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.background)
}