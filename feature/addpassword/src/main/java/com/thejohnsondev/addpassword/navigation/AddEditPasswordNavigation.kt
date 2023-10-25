package com.thejohnsondev.addpassword.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.thejohnsondev.addpassword.AddEditPasswordScreen
import com.thejohnsondev.addpassword.AddEditPasswordViewModel
import com.thejohnsondev.common.fromJson
import com.thejohnsondev.common.navigation.Screens
import com.thejohnsondev.model.PasswordModel

val addEditPasswordRoute = Screens.AddEditPassword.name

fun NavController.navigateToAddEditPassword(password: String?, navOptions: NavOptions? = null) {
    navigate("$addEditPasswordRoute/$password", navOptions)
}

fun NavGraphBuilder.addEditPasswordScreen(
    onGoBackClick: () -> Unit
) {
    composable(
        route = "$addEditPasswordRoute/{password}"
    ) { navBackStackEntry ->
        val passwordModel = navBackStackEntry.arguments?.getString("password")
            .fromJson<PasswordModel?>()
        val viewModel = hiltViewModel<AddEditPasswordViewModel>()
        AddEditPasswordScreen(
            viewModel = viewModel,
            passwordModel = passwordModel,
            onGoBackClick = onGoBackClick
        )
    }
}