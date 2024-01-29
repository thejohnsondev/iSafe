package com.thejohnsondev.isafe.presentation.navigation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.thejohnsondev.common.R
import com.thejohnsondev.designsystem.Size16
import com.thejohnsondev.designsystem.Size48
import com.thejohnsondev.designsystem.Size86
import com.thejohnsondev.ui.ScaffoldConfig
import com.thejohnsondev.ui.bounceClick

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    scaffoldState: State<ScaffoldConfig>,
    navController: NavHostController,
    bottomBarState: MutableState<Boolean>,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedVisibility(visible = scaffoldState.value.isTopAppBarVisible) {
                TopAppBar(
                    title = {
                        scaffoldState.value.topAppBarTitle?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
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
            if (scaffoldState.value.isBottomNavBarVisible) {
                ISafeBottomNavigation(
                    navController = navController,
                    bottomBarState = bottomBarState
                )
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
        content(it)
    }
}