package com.thejohnsondev.isafe.presentation.navigation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.thejohnsondev.common.R
import com.thejohnsondev.designsystem.DrawerWidth
import com.thejohnsondev.designsystem.Size12
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size32
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.Size8
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.ui.ISafeLogo
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.bounceClick
import com.thejohnsondev.ui.conditional

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    windowSize: WindowWidthSizeClass,
    scaffoldState: State<ScaffoldConfig>,
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior,
    selectedItemIndex: MutableState<Int>,
    content: @Composable (PaddingValues) -> Unit,
) {
    val navigationItems = listOf(
        BottomNavItem.Passwords,
        BottomNavItem.Notes,
        BottomNavItem.Settings
    )
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            if (scaffoldState.value.isTopAppBarVisible) {
                TopAppBar(
                    modifier = Modifier
                        .conditional(windowSize == WindowWidthSizeClass.Expanded) {
                            Modifier.padding(start = DrawerWidth)
                        },
                    title = {
                        scaffoldState.value.topAppBarTitle?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    },
                    navigationIcon = {
                        scaffoldState.value.topAppBarIcon?.let {
                            Icon(
                                modifier = Modifier
                                    .size(Size48)
                                    .padding(start = Size16)
                                    .clip(CircleShape)
                                    .bounceClick()
                                    .clickable {
                                        scaffoldState.value.onTopAppBarIconClick()
                                    },
                                imageVector = it,
                                contentDescription = ""
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    actions = {
                        if (scaffoldState.value.isTopAppBarSaveButtonVisible) {
                            Button(
                                modifier = Modifier
                                    .padding(Size16)
                                    .bounceClick(),
                                onClick = {
                                    scaffoldState.value.onTopAppBarSaveClick()
                                }) {
                                Text(text = stringResource(R.string.save))
                            }
                        }
                        if (scaffoldState.value.isTopAppBarDeleteButtonVisible) {
                            IconButton(
                                modifier = Modifier.padding(end = Size16),
                                onClick = {
                                    scaffoldState.value.onTopAppBarDeleteClick()
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            if (scaffoldState.value.isFabVisible) {
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .bounceClick(),
                    onClick = {
                        scaffoldState.value.onFabClick()
                    },
                    expanded = scaffoldState.value.isFabExpanded,
                    icon = {
                        scaffoldState.value.fabIcon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = null
                            )
                        }
                    },
                    text = {
                        scaffoldState.value.fabTitle?.let {
                            Text(text = it)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            if (scaffoldState.value.isBottomNavBarVisible
                && windowSize == WindowWidthSizeClass.Compact
            ) {
                AnimatedVisibility(
                    visible = bottomBarState.value,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it }),
                ) {
                    NavigationBar {
                        navigationItems.forEachIndexed { index, screen ->
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
        },
        snackbarHost = {
            scaffoldState.value.snackBarHostState?.let { snackBarHostState ->
                SnackbarHost(snackBarHostState) { data ->
                    Snackbar(
                        modifier = Modifier
                            .padding(
                                vertical = scaffoldState.value.snackBarPaddingVertical ?: Size86,
                                horizontal = scaffoldState.value.snackBarPaddingHorizontal ?: Size16
                            )
                    ) {
                        Text(text = data.visuals.message)
                    }
                }
            }
        }
    ) {
        when (windowSize) {
            WindowWidthSizeClass.Expanded -> {
                PermanentNavigationDrawer(
                    drawerContent = {
                        PermanentDrawerSheet(
                            modifier = Modifier
                                .width(DrawerWidth),
                            drawerContainerColor = MaterialTheme.colorScheme.surfaceDim,
                            drawerShape = RoundedCornerShape(topEnd = Size32, bottomEnd = Size32),
                        ) {
                            Column(
                                modifier = Modifier.padding(Size8)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                ISafeLogo(
                                    fontSize = MaterialTheme.typography.displaySmall.fontSize,
                                )
                            }
                            navigationItems.forEachIndexed { index, screen ->
                                NavigationDrawerItem(
                                    label = { Text(text = stringResource(screen.titleRes)) },
                                    selected = selectedItemIndex.value == index,
                                    onClick = {
                                        selectedItemIndex.value = index
                                        navController.navigate(screen.route)
                                    },
                                    icon = {
                                        Icon(
                                            painter = painterResource(id = screen.imgResId),
                                            contentDescription = stringResource(
                                                id = screen.titleRes
                                            )
                                        )
                                    },
                                    modifier = Modifier
                                        .padding(horizontal = Size12, vertical = Size8)
                                )
                            }

                        }
                    }
                ) {
                    content(it)
                }
            }
            WindowWidthSizeClass.Medium -> {
                // todo show nav rail
                content(it)
            }
            else -> {
                content(it)
            }
        }
    }
}