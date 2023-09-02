package com.thejohnsondev.isafe.presentation.screens.feat.home

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.thejohnsondev.isafe.presentation.navigation.ISafeBottomNavigation
import com.thejohnsondev.isafe.presentation.navigation.Screens
import com.thejohnsondev.isafe.presentation.screens.feat.notes.NotesScreen
import com.thejohnsondev.isafe.presentation.screens.feat.notes.NotesViewModel
import com.thejohnsondev.isafe.presentation.screens.feat.passwords.PasswordsScreen
import com.thejohnsondev.isafe.presentation.screens.feat.passwords.PasswordsViewModel
import com.thejohnsondev.isafe.presentation.screens.feat.settings.SettingsScreen
import com.thejohnsondev.isafe.presentation.screens.feat.settings.SettingsViewModel
import com.thejohnsondev.isafe.utils.TWEEN_ANIM_DEFAULT_DURATION

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val navController = rememberAnimatedNavController()
    Scaffold(
        bottomBar = {
            ISafeBottomNavigation(navController = navController)
        }
    ) {
        AnimatedNavHost(
            navController = navController,
            startDestination = Screens.NotesScreen.name,
            modifier = Modifier.padding(it)
        ) {
            composable(
                route = Screens.NotesScreen.name,
                enterTransition = {
                    fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                }
            ) {
                val viewModel = hiltViewModel<NotesViewModel>()
                NotesScreen(navController = navController, viewModel = viewModel)
            }
            composable(
                route = Screens.PasswordsScreen.name,
                enterTransition = {
                    fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                }
            ) {
                val viewModel = hiltViewModel<PasswordsViewModel>()
                PasswordsScreen(navController = navController, viewModel = viewModel)
            }
            composable(
                route = Screens.Settings.name,
                enterTransition = {
                    fadeIn(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(TWEEN_ANIM_DEFAULT_DURATION))
                }
            ) {
                val viewModel = hiltViewModel<SettingsViewModel>()
                SettingsScreen(navController = navController, viewModel = viewModel)
            }
        }

    }
}