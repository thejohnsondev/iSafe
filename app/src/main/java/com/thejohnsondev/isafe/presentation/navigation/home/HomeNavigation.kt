package com.thejohnsondev.isafe.presentation.navigation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
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
import com.thejohnsondev.ui.ScaffoldConfig

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeNavigation(rootNavController: NavController) {
    val navController = rememberNavController()
    val bottomBarState = rememberSaveable {
        (mutableStateOf(true))
    }
    val scaffoldState = remember {
        mutableStateOf(ScaffoldConfig())
    }
    val selectedItemIndex = rememberSaveable {
        mutableIntStateOf(0)
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Surface(modifier = Modifier
        .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        HomeScaffold(
            scaffoldState = scaffoldState,
            navController = navController,
            bottomBarState = bottomBarState,
            scrollBehavior = scrollBehavior,
            selectedItemIndex = selectedItemIndex
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = vaultRoute,
                modifier = Modifier.padding(paddingValues)
            ) {
                vaultScreen(
                    onAddNewPasswordClick = {
                        navController.navigateToAddEditPassword(password = null)
                    },
                    onEditPasswordClick = {
                        navController.navigateToAddEditPassword(password = it.toJson())
                    },
                    setScaffoldConfig = { scaffold ->
                        scaffoldState.value = scaffold
                    }
                )
                addEditPasswordScreen(
                    onGoBackClick = {
                        navController.popBackStack()
                    },
                    setScaffoldConfig = { scaffold ->
                        scaffoldState.value = scaffold
                    }
                )
                addNoteScreen(
                    goBack = {
                        navController.popBackStack()
                    },
                    setScaffoldConfig = { scaffold ->
                        scaffoldState.value = scaffold
                    }
                )
                notesScreen(
                    goToAddNote = {
                        navController.navigateToAddNote(note = null)
                    },
                    onNoteClick = {
                        navController.navigateToAddNote(note = it.toJson())
                    },
                    setScaffoldConfig = { scaffold ->
                        scaffoldState.value = scaffold
                    }
                )
                settingsScreen(
                    goToSignUp = {
                        rootNavController.navigateToSignUp()
                    },
                    setScaffoldConfig = { scaffold ->
                        scaffoldState.value = scaffold
                    }
                )
            }
        }
    }
}