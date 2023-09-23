package com.thejohnsondev.isafe.presentation.navigation

import com.thejohnsondev.isafe.R

sealed class BottomNavItem(
    val route: String,
    val titleRes: Int,
    val imgResId: Int
) {
    object Notes : BottomNavItem(
        route = Screens.NotesScreen.name,
        titleRes = R.string.notes,
        imgResId = R.drawable.ic_notes
    )

    object Passwords :
        BottomNavItem(
            route = Screens.VaultScreen.name,
            titleRes = R.string.vault,
            imgResId = R.drawable.ic_security
        )

    object Settings :
        BottomNavItem(
            route = Screens.Settings.name,
            titleRes = R.string.settings,
            imgResId = R.drawable.ic_settings
        )
}
