package com.thejohnsondev.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

data class ScaffoldConfig(
    val isBottomNavBarVisible: Boolean = false,
    val isTopAppBarVisible: Boolean = false,
    val isTopAppBarSaveButtonVisible: Boolean = false,
    val onTopAppBarSaveClick: () -> Unit = { },
    val isTopAppBarDeleteButtonVisible: Boolean = false,
    val onTopAppBarDeleteClick: () -> Unit = { },
    val topAppBarTitle: String? = null,
    val topAppBarIcon: ImageVector? = null,
    val onTopAppBarIconClick: () -> Unit = { },
    val isFabVisible: Boolean = false,
    val fabTitle: String? = null,
    val fabIcon: ImageVector? = null,
    val onFabClick: () -> Unit = { },
    val isFabExpanded: Boolean = false,
    val snackBarPaddingHorizontal: Dp? = null,
    val snackBarPaddingVertical: Dp? = null,
    val snackBarHostState: SnackbarHostState? = null,
    val bottomBarItemIndex: Int = 0
)