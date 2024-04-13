package com.thejohnsondev.isafe.presentation

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.thejohnsondev.designsystem.ISafeTheme
import com.thejohnsondev.isafe.presentation.navigation.Navigation
import com.thejohnsondev.model.settings.DarkThemeConfig
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setupContent()
    }

    private fun setupContent() {

        var state: MainActivityViewModel.State by mutableStateOf(MainActivityViewModel.State())

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { newState ->
                    state = newState
                }
            }
        }

        setContent {
            ISafeTheme(
                darkTheme = shouldUseDarkTheme(state),
                dynamicColor = shouldUseDynamicColor(state),
                customTheme = state.settingsConfig?.customTheme
            ) {
                ISafeApp()
            }
        }
    }
}

@Composable
fun ISafeApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Navigation()
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    state: MainActivityViewModel.State,
): Boolean = when (state.settingsConfig?.darkThemeConfig) {
    DarkThemeConfig.LIGHT -> false
    DarkThemeConfig.DARK -> true
    else -> isSystemInDarkTheme()
}

@Composable
private fun shouldUseDynamicColor(
    state: MainActivityViewModel.State,
): Boolean = state.settingsConfig?.useDynamicColor ?: false
