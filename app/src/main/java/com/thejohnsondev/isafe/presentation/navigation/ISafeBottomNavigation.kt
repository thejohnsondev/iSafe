package com.thejohnsondev.isafe.presentation.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController

@Composable
fun ISafeBottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomNavItem.Notes,
        BottomNavItem.Passwords,
        BottomNavItem.Settings
    )
    NavigationBar {
        val selectedItemIndex = rememberSaveable {
            mutableStateOf(0)
        }
        items.forEachIndexed { index, screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.imgResId),
                        contentDescription = stringResource(
                            id = screen.titleRes
                        )
                    )
                },
                label = { Text(stringResource(screen.titleRes)) },
                selected = selectedItemIndex.value == index,
                onClick = {
                    selectedItemIndex.value = index
                    navController.navigate(screen.route)
                },
            )
        }
    }
}
