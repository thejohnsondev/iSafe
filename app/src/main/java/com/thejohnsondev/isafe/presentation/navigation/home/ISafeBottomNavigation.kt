package com.thejohnsondev.isafe.presentation.navigation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController

@Composable
fun ISafeBottomNavigation(
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    selectedItemIndex: MutableState<Int>
) {
    val items = listOf(
        BottomNavItem.Passwords,
        BottomNavItem.Notes,
        BottomNavItem.Settings
    )
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
    ) {
        NavigationBar {
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
}
