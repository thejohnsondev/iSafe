package com.thejohnsondev.ui.scaffold

import com.thejohnsondev.common.navigation.Screens

sealed class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val imgResId: Int,
    val index: Int
) {
    data object Vault : BottomNavItem(
        route = Screens.VaultScreen.name,
        titleRes = com.thejohnsondev.common.R.string.vault,
        imgResId = com.thejohnsondev.designsystem.R.drawable.ic_security,
        index = 0
    )

    data object Notes : BottomNavItem(
        route = Screens.NotesScreen.name,
        titleRes = com.thejohnsondev.common.R.string.notes,
        imgResId = com.thejohnsondev.designsystem.R.drawable.ic_notes,
        index = 1
    )

    data object Settings : BottomNavItem(
        route = Screens.Settings.name,
        titleRes = com.thejohnsondev.common.R.string.settings,
        imgResId = com.thejohnsondev.designsystem.R.drawable.ic_settings,
        index = 2
    )
}
