package com.thejohnsondev.presentation

import arrow.core.Either
import com.thejohnsondev.data.AuthRepository
import com.thejohnsondev.data.FakeSettingsRepository
import com.thejohnsondev.datastore.FakeDataStore
import com.thejohnsondev.domain.ChangePasswordUseCase
import com.thejohnsondev.domain.DeleteAccountUseCase
import com.thejohnsondev.domain.GetSettingsConfigFlowUseCase
import com.thejohnsondev.domain.GetUserEmailUseCase
import com.thejohnsondev.domain.LogoutUseCase
import com.thejohnsondev.domain.SettingsUseCases
import com.thejohnsondev.domain.UpdateSettingsUseCase
import com.thejohnsondev.model.HttpError
import com.thejohnsondev.model.OneTimeEvent
import com.thejohnsondev.model.settings.DarkThemeConfig
import com.thejohnsondev.model.settings.GeneralSettings
import com.thejohnsondev.model.settings.PrivacySettings
import com.thejohnsondev.model.settings.ThemeBrand
import com.thejohnsondev.test.MainDispatcherRule
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class SettingsViewModelTest {

    @get:Rule
    var mockitoRule: MockitoRule = MockitoJUnit.rule()

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val mockAuthRepository = Mockito.mock(AuthRepository::class.java)

    private val useCases = SettingsUseCases(
        LogoutUseCase(mockAuthRepository),
        DeleteAccountUseCase(mockAuthRepository),
        ChangePasswordUseCase(mockAuthRepository),
        GetUserEmailUseCase(FakeDataStore()),
        GetSettingsConfigFlowUseCase(FakeSettingsRepository()),
        UpdateSettingsUseCase(FakeSettingsRepository())
    )

    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SettingsViewModel(useCases)
    }

    @Test
    fun `fetchData updates userEmail`() = runTest {
        viewModel.perform(SettingsViewModel.Action.FetchData)

        assertEquals("test@email.com", viewModel.viewState.value.userEmail)
    }

    @Test
    fun `openConfirmDeleteAccountDialog sets dialog state to true`() = runTest {
        viewModel.perform(SettingsViewModel.Action.OpenConfirmDeleteAccountDialog)

        assertEquals(true, viewModel.viewState.value.openConfirmDeleteAccountDialog)
    }

    @Test
    fun `closeConfirmDeleteAccountDialog sets dialog state to false`() = runTest {
        viewModel.perform(SettingsViewModel.Action.CloseConfirmDeleteAccountDialog)

        assertEquals(false, viewModel.viewState.value.openConfirmDeleteAccountDialog)
    }

    @Test
    fun `openConfirmLogoutDialog sets dialog state to true`() = runTest {
        viewModel.perform(SettingsViewModel.Action.OpenConfirmLogoutDialog)

        assertEquals(true, viewModel.viewState.value.openConfirmLogoutDialog)
    }

    @Test
    fun `closeConfirmLogoutDialog sets dialog state to false`() = runTest {
        viewModel.perform(SettingsViewModel.Action.CloseConfirmLogoutDialog)

        assertEquals(false, viewModel.viewState.value.openConfirmLogoutDialog)
    }

    @Test
    fun `updateUseCustomTheme updates theme in settings`() = runTest {
        val themeBrand = ThemeBrand.ANDROID

        viewModel.perform(SettingsViewModel.Action.UpdateUseCustomTheme(themeBrand))

        assertEquals(themeBrand, viewModel.viewState.value.settingsConfig?.customTheme)
    }

    @Test
    fun `updateUseDynamicColor updates dynamic color setting`() = runTest {
        viewModel.perform(SettingsViewModel.Action.UpdateUseDynamicColor(true))

        assertEquals(true, viewModel.viewState.value.settingsConfig?.useDynamicColor)
    }

    @Test
    fun `updateDarkThemeConfig updates dark theme config`() = runTest {
        val darkThemeConfig = DarkThemeConfig.DARK

        viewModel.perform(SettingsViewModel.Action.UpdateDarkThemeConfig(darkThemeConfig))

        assertEquals(darkThemeConfig, viewModel.viewState.value.settingsConfig?.darkThemeConfig)
    }

    @Test
    fun `updateGeneralSettings updates general settings`() = runTest {
        val generalSettings = GeneralSettings(isDeepSearchEnabled = false)

        viewModel.perform(SettingsViewModel.Action.UpdateGeneralSettings(generalSettings))

        assertEquals(generalSettings, viewModel.viewState.value.settingsConfig?.generalSettings)
    }

    @Test
    fun `updatePrivacySettings updates privacy settings`() = runTest {
        val privacySettings =
            PrivacySettings(isUnlockWithBiometricEnabled = false, isBlockScreenshotsEnabled = true)

        viewModel.perform(SettingsViewModel.Action.UpdatePrivacySettings(privacySettings))


        assertEquals(privacySettings, viewModel.viewState.value.settingsConfig?.privacySettings)
    }

    @Test
    fun `delete account sends success navigation event`() = runTest {
        `when`(mockAuthRepository.deleteAccount()).thenReturn(flowOf(Either.Right(Unit)))

        viewModel.perform(SettingsViewModel.Action.DeleteAccount)

        assertTrue(viewModel.getEventFlow().value is OneTimeEvent.SuccessNavigation)
    }

    @Test
    fun `delete account sends error message`() = runTest {
        `when`(mockAuthRepository.deleteAccount()).thenReturn(
            flowOf(
                Either.Left(
                    HttpError(
                        500,
                        "Error deleting account"
                    )
                )
            )
        )

        viewModel.perform(SettingsViewModel.Action.DeleteAccount)

        assertTrue(viewModel.getEventFlow().value is OneTimeEvent.InfoToast)
    }

    @Test
    fun `logout sends success navigation event`() = runTest {
        viewModel.perform(SettingsViewModel.Action.Logout)

        assertEquals(OneTimeEvent.SuccessNavigation::class, viewModel.getEventFlow().value::class)
    }

    // TODO add test case: change password returns success
}