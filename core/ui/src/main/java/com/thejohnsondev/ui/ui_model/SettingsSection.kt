package com.thejohnsondev.ui.ui_model

import com.thejohnsondev.common.R

data class SettingsSection(
    val sectionTitleRes: Int,
    val subsections: List<SettingsSubSection>
) {
    companion object {
        fun getSettingsSections(): List<SettingsSection> = listOf(
            SettingsSection(
                sectionTitleRes = R.string.account,
                subsections = listOf(
                    SettingsSubSection.ManageAccountSub
                )
            ),
            SettingsSection(
                sectionTitleRes = R.string.settings,
                subsections = listOf(
                    SettingsSubSection.GeneralSettingsSub,
                    SettingsSubSection.StyleSettingsSub,
                    SettingsSubSection.PrivacySettingsSub
                )
            )
        )
    }
}