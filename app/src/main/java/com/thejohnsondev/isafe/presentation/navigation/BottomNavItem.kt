package com.thejohnsondev.isafe.presentation.navigation

import com.thejohnsondev.common.navigation.Screens

sealed class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val imgResId: Int
) {
    object Notes : BottomNavItem(
        route = Screens.NotesScreen.name,
        titleRes = com.thejohnsondev.common.R.string.notes,
        imgResId = com.thejohnsondev.designsystem.R.drawable.ic_notes
    )

    object Passwords :
        BottomNavItem(
            route = Screens.VaultScreen.name,
            titleRes = com.thejohnsondev.common.R.string.vault,
            imgResId = com.thejohnsondev.designsystem.R.drawable.ic_security
        )

    object Settings :
        BottomNavItem(
            route = Screens.Settings.name,
            titleRes = com.thejohnsondev.common.R.string.settings,
            imgResId = com.thejohnsondev.designsystem.R.drawable.ic_settings
        )
}
