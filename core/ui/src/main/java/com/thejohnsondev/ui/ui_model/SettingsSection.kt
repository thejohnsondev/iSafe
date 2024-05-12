package com.thejohnsondev.ui.ui_model

import com.thejohnsondev.common.R

data class SettingsSection(
    val sectionTitleRes: Int? = null,
    val subsections: List<SettingsSubSection>
) {
    companion object {
        fun getSettingsSections(): List<SettingsSection> = listOf(
            SettingsSection(
                subsections = listOf(
                    SettingsSubSection.GeneralSettingsSub,
                    SettingsSubSection.StyleSettingsSub,
                    SettingsSubSection.PrivacySettingsSub
                )
            ),
            SettingsSection(
                sectionTitleRes = R.string.account,
                subsections = listOf(
                    SettingsSubSection.ManageAccountSub
                )
            )
        )
    }
}